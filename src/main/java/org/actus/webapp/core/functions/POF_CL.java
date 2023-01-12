package org.actus.webapp.core.functions;

import org.actus.functions.PayOffFunction;
import org.actus.states.StateSpace;
import org.actus.attributes.ContractModelProvider;
import org.actus.externals.RiskFactorModelProvider;
import org.actus.conventions.daycount.DayCountCalculator;
import org.actus.conventions.businessday.BusinessDayAdjuster;

import java.time.LocalDateTime;

public final class POF_CL implements PayOffFunction {
    Double weight;

    public POF_CL() {
        this.weight = 1.0;
    }

    public POF_CL(Double weight) {
        this.weight = weight;
    }

    @Override
        public double eval(LocalDateTime time, StateSpace states, 
    ContractModelProvider model, RiskFactorModelProvider riskFactorModel, DayCountCalculator dayCounter, BusinessDayAdjuster timeAdjuster) {
        double timeFromLastEvent = dayCounter.dayCountFraction(timeAdjuster.shiftCalcTime(states.statusDate), timeAdjuster.shiftCalcTime(time));
        states.accruedInterest += states.nominalInterestRate * states.notionalPrincipal * timeFromLastEvent;
        states.feeAccrued += model.<Double>getAs("FeeRate") * states.notionalPrincipal * timeFromLastEvent;

        double lossFactor = weight * riskFactorModel.stateAt(model.<String>getAs("ObjectCodeOfCreditLossModel"),time,states,model);
        double loss = -lossFactor * states.notionalPrincipal;

        return loss;
        }
}

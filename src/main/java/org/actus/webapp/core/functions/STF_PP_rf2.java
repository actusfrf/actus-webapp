package org.actus.webapp.core.functions;

import java.time.LocalDateTime;

import org.actus.attributes.ContractModelProvider;
import org.actus.conventions.businessday.BusinessDayAdjuster;
import org.actus.conventions.daycount.DayCountCalculator;
import org.actus.externals.RiskFactorModelProvider;
import org.actus.functions.StateTransitionFunction;
import org.actus.states.StateSpace;

public final class STF_PP_rf2 implements StateTransitionFunction {
	private String riskFactorID;   // the id of the behavior model for this prepayment callout event 
	public STF_PP_rf2(String riskFactorID) {
		this.riskFactorID = riskFactorID;
	} 
    @Override
    public StateSpace eval(LocalDateTime time, StateSpace states,
    ContractModelProvider model, RiskFactorModelProvider riskFactorModel, DayCountCalculator dayCounter, BusinessDayAdjuster timeAdjuster) {
        // update state space
        double timeFromLastEvent = dayCounter.dayCountFraction(timeAdjuster.shiftCalcTime(states.statusDate), timeAdjuster.shiftCalcTime(time));
        states.accruedInterest += states.nominalInterestRate * states.notionalPrincipal * timeFromLastEvent;
        states.feeAccrued += model.<Double>getAs("FeeRate") * states.notionalPrincipal * timeFromLastEvent;
        states.notionalPrincipal -= riskFactorModel.stateAt(this.riskFactorID,time,states,model,false) * states.notionalPrincipal;
        states.statusDate = time;

        // return post-event-states
        return StateSpace.copyStateSpace(states);
    }
}

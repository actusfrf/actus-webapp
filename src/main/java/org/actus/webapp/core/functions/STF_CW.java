package org.actus.webapp.core.functions;

import org.actus.functions.StateTransitionFunction;
import org.actus.states.StateSpace;
import org.actus.attributes.ContractModelProvider;
import org.actus.externals.RiskFactorModelProvider;
import org.actus.conventions.daycount.DayCountCalculator;
import org.actus.conventions.businessday.BusinessDayAdjuster;
import org.actus.conventions.contractrole.ContractRoleConvention;
import org.actus.types.ContractRole;

import java.time.LocalDateTime;

public final class STF_CW implements StateTransitionFunction {
    
    @Override
    public StateSpace eval(LocalDateTime time, StateSpace states,
    ContractModelProvider model, RiskFactorModelProvider riskFactorModel, DayCountCalculator dayCounter, BusinessDayAdjuster timeAdjuster) {
        // update state space
        double timeFromLastEvent = dayCounter.dayCountFraction(timeAdjuster.shiftCalcTime(states.statusDate), timeAdjuster.shiftCalcTime(time));
        states.accruedInterest += states.nominalInterestRate * states.notionalPrincipal * timeFromLastEvent;
        states.feeAccrued += model.<Double>getAs("FeeRate") * states.notionalPrincipal * timeFromLastEvent;
        double roamt = riskFactorModel.stateAt(model.getAs("ObjectCodeOfCashBalanceModel"),time,states,model) ;
        double absnp = states.notionalPrincipal*ContractRoleConvention.roleSign(model.getAs("ContractRole")) ;
        double payoff = ((absnp + roamt ) < 0.0) ?   -1.0 * absnp : roamt ;
	states.notionalPrincipal += ContractRoleConvention.roleSign(model.getAs("ContractRole"))* payoff  ; 
	states.statusDate = time;

        // return post-event-states
        return StateSpace.copyStateSpace(states);
    }
}

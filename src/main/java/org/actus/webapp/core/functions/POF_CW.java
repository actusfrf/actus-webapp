package org.actus.webapp.core.functions;

import org.actus.functions.PayOffFunction;
import org.actus.states.StateSpace;
import org.actus.attributes.ContractModelProvider;
import org.actus.externals.RiskFactorModelProvider;
import org.actus.conventions.daycount.DayCountCalculator;
import org.actus.conventions.businessday.BusinessDayAdjuster;
import org.actus.conventions.contractrole.ContractRoleConvention;
import org.actus.types.ContractRole;
import org.actus.util.CommonUtils;

import java.time.LocalDateTime;

public final class POF_CW implements PayOffFunction {
    
    @Override
        public double eval(LocalDateTime time, StateSpace states, 
    ContractModelProvider model, RiskFactorModelProvider riskFactorModel, DayCountCalculator dayCounter, BusinessDayAdjuster timeAdjuster) {
        double roamt = riskFactorModel.stateAt(model.getAs("ObjectCodeOfCashBalanceModel"),time,states,model);
        double absnp = states.notionalPrincipal*ContractRoleConvention.roleSign(model.getAs("ContractRole"));
	double payoff = ((absnp + roamt ) < 0.0) ?   -1.0 * absnp : roamt ;
System.out.println("****fnp030 absnp =<" + absnp + "> roamt= <" + roamt +"> payoff = <" + payoff +">");          // fnp diagnostic jan 2023
System.out.println("****fnp031 signP value =<"+ ContractRoleConvention.roleSign(model.getAs("ContractRole")) + ">") ;  // fnp diagnostic jan 2023  
        return payoff;
        }
}

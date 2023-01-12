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

public final class POF_PP implements PayOffFunction {
    
    @Override
        public double eval(LocalDateTime time, StateSpace states, 
    ContractModelProvider model, RiskFactorModelProvider riskFactorModel, DayCountCalculator dayCounter, BusinessDayAdjuster timeAdjuster) {
System.out.println("****fnp020 PayoffFunction POF_PP entered");       // fnp diagnostic jan 2023   
        return CommonUtils.settlementCurrencyFxRate(riskFactorModel, model, time, states)
                * ContractRoleConvention.roleSign(model.getAs("ContractRole"))
                * riskFactorModel.stateAt(model.getAs("ObjectCodeOfPrepaymentModel"),time,states,model)
                * states.notionalPrincipal;
        }
}

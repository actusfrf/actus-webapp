package org.actus.webapp.core.functions;

import java.time.LocalDateTime;

import org.actus.attributes.ContractModelProvider;
import org.actus.conventions.businessday.BusinessDayAdjuster;
import org.actus.conventions.contractrole.ContractRoleConvention;
import org.actus.conventions.daycount.DayCountCalculator;
import org.actus.externals.RiskFactorModelProvider;
import org.actus.functions.PayOffFunction;
import org.actus.states.StateSpace;
import org.actus.util.CommonUtils;

public final class POF_PP_rf2 implements PayOffFunction {
	private String riskFactorID;   // the id of the behavior model for this prepayment callout event 
	public POF_PP_rf2(String riskFactorID) {
		this.riskFactorID = riskFactorID;
	}
    @Override    
        public double eval(LocalDateTime time, StateSpace states, ContractModelProvider model,
        		           RiskFactorModelProvider riskFactorModel, DayCountCalculator dayCounter,
        		           BusinessDayAdjuster timeAdjuster) {
        	System.out.println("****fnp110 PayoffFunction POF_PP_rf2 entered");       // fnp diagnostic jan 2023   
        	return CommonUtils.settlementCurrencyFxRate(riskFactorModel, model, time, states)
                * ContractRoleConvention.roleSign(model.getAs("ContractRole"))
                * riskFactorModel.stateAt(this.riskFactorID,time,states,model,false)
                * states.notionalPrincipal;
        	}
}

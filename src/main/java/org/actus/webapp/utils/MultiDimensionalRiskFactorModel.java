package org.actus.webapp.utils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Set;

import org.actus.attributes.ContractModelProvider;
import org.actus.externals.RiskFactorModelProvider;
import org.actus.states.StateSpace;

public class MultiDimensionalRiskFactorModel implements RiskFactorModelProvider {
	HashMap<String,RiskFactorModelProvider> model = new HashMap<String,RiskFactorModelProvider>();
	
	public MultiDimensionalRiskFactorModel() {
	}

	public Set<String> keys() {
		return model.keySet();
	}

	public void add(String symbol, RiskFactorModelProvider dimension) {
		model.put(symbol,dimension);
	}

	public double stateAt(String id, LocalDateTime time, StateSpace states,
			ContractModelProvider terms, boolean isMarket) {
		return model.get(id).stateAt(id,time,states,terms, isMarket);
	}
}
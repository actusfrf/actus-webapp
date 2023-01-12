package org.actus.webapp.utils;

import java.util.Set;
import java.util.HashMap;
import java.time.LocalDateTime;

import org.actus.states.StateSpace;
import org.actus.attributes.ContractModelProvider;
import org.actus.externals.RiskFactorModelProvider;

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
			ContractModelProvider terms) {
		return model.get(id).stateAt(id,time,states,terms);
	}
}
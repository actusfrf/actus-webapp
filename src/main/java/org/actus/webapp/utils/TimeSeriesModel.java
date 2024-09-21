package org.actus.webapp.utils;

import java.time.LocalDateTime;
import java.util.Set;

import org.actus.attributes.ContractModelProvider;
import org.actus.externals.RiskFactorModelProvider;
import org.actus.states.StateSpace;
import org.actus.webapp.models.ObservedData;

public class TimeSeriesModel implements RiskFactorModelProvider {
	String riskFactorId;
	TimeSeries<LocalDateTime,Double> timeSeries;
	
	public TimeSeriesModel(){
	}

	public TimeSeriesModel(ObservedData data) {
		this.riskFactorId = data.getMarketObjectCode();
		Double base = data.getBase();
		LocalDateTime[] times = data.getData().stream().map(obs -> LocalDateTime.parse(obs.getTime())).toArray(LocalDateTime[]::new);
        Double[] values = data.getData().stream().map(obs -> 1/base*obs.getValue()).toArray(Double[]::new);  
        this.timeSeries = new TimeSeries<LocalDateTime,Double>();
        this.timeSeries.of(times,values);
	}
	
	public Set<String> keys() {
		return Set.of(this.riskFactorId);
	}

	public double stateAt(String id, LocalDateTime time, StateSpace states,
			ContractModelProvider terms, boolean isMarket) {
		return timeSeries.getValueFor(time,1);
	}
}
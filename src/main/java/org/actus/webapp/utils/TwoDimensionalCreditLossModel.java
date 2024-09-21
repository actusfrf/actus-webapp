package org.actus.webapp.utils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.actus.attributes.ContractModelProvider;
import org.actus.externals.RiskFactorModelProvider;
import org.actus.states.StateSpace;
import org.actus.webapp.models.TwoDimensionalCreditLossModelData;

public class TwoDimensionalCreditLossModel implements RiskFactorModelProvider {
	String macroIndex;
	String riskFactorId;
	HashMap<Integer,HashMap<Integer,Double>> surface;
	RiskFactorModelProvider marketModel;
	
	public TwoDimensionalCreditLossModel() {
	}

	public TwoDimensionalCreditLossModel(String riskFactorId, TwoDimensionalCreditLossModelData data, RiskFactorModelProvider marketModel) {
		this.marketModel = marketModel;
		this.riskFactorId = riskFactorId;
		this.macroIndex = data.getMacroIndexId();
		this.surface = new HashMap<Integer,HashMap<Integer,Double>>();
		Integer[] dimension1Margins = data.getSurface().getMargins().get(0).getValues().stream().map(obs -> obs.intValue()).toArray(Integer[]::new);
		Integer[] dimension2Margins = data.getSurface().getMargins().get(1).getValues().stream().map(obs -> obs.intValue()).toArray(Integer[]::new);
		List<List<Double>> values = data.getSurface().getData();
		for(int i=0; i<values.size(); i++) {
			HashMap<Integer,Double> dimensionSeries = new HashMap<Integer,Double>();
			Double[] dimensionValues = values.get(i).stream().map(obs -> obs).toArray(Double[]::new);
			for(int j=0; j<dimension2Margins.length; j++) {
				dimensionSeries.put(dimension2Margins[j],dimensionValues[j]);
			}
			//dimensionSeries.of(dimension2Margins,dimensionValues);
			surface.put(dimension1Margins[i],dimensionSeries);
		}
	}
	
	public Set<String> keys() {
		return Set.of(this.riskFactorId);
	}

	public double stateAt(String id, LocalDateTime time, StateSpace states,
			ContractModelProvider terms, boolean isMarket) {
		double macroIndex = 1;
		if(!this.macroIndex.equals("")) macroIndex = marketModel.stateAt(this.macroIndex,time,states,terms,true);
		// looks like a call to get a market rate - we are implementing the behavior stateAt() here 
		Integer loanType = terms.<Integer>getAs("loanType");
		Integer riskGrade = terms.<Integer>getAs("riskGrade");
		double lossFactor = surface.get(loanType).get(riskGrade);
		return macroIndex * lossFactor;
	}
}
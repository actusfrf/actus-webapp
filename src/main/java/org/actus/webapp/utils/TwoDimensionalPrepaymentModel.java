package org.actus.webapp.utils;

import java.util.Set;
import java.util.List;
import java.time.LocalDateTime;

import org.actus.states.StateSpace;
import org.actus.webapp.models.ObservedData;
import org.actus.webapp.models.TwoDimensionalPrepaymentModelData;
import org.actus.webapp.models.TwoDimensionalSurfaceData;
import org.actus.attributes.ContractModelProvider;
import org.actus.conventions.daycount.DayCountCalculator;
import org.actus.externals.RiskFactorModelProvider;

public class TwoDimensionalPrepaymentModel implements RiskFactorModelProvider {
	String referenceRate;
	String riskFactorId;
	TimeSeries<Double,TimeSeries<Double,Double>> surface;
	RiskFactorModelProvider marketModel;
	DayCountCalculator dayCount;
	
	public TwoDimensionalPrepaymentModel() {
	}

	public TwoDimensionalPrepaymentModel(String riskFactorId, TwoDimensionalPrepaymentModelData data, RiskFactorModelProvider marketModel) {
		this.marketModel = marketModel;
		this.riskFactorId = riskFactorId;
		this.referenceRate = data.getReferenceRateId();
		this.dayCount = new DayCountCalculator("A360", null);
		this.surface = new TimeSeries<Double,TimeSeries<Double,Double>>();
		List<Double> dimension1Margins = data.getSurface().getMargins().get(0).getValues();
		Double[] dimension2Margins = data.getSurface().getMargins().get(1).getValues().stream().map(obs -> obs).toArray(Double[]::new);
		List<List<Double>> values = data.getSurface().getData();
		for(int i=0; i<values.size(); i++) {
			TimeSeries<Double,Double> dimensionSeries = new TimeSeries<Double,Double>();
			Double[] dimensionValues = values.get(i).stream().map(obs -> obs).toArray(Double[]::new);
			dimensionSeries.of(dimension2Margins,dimensionValues);
			surface.put(dimension1Margins.get(i),dimensionSeries);
		}
	}
	
	public Set<String> keys() {
		return Set.of(this.riskFactorId);
	}

	public double stateAt(String id, LocalDateTime time, StateSpace states,
			ContractModelProvider terms) {
System.out.println("****fnp009 TwoDimensionalPrepaymentModel stateAt() entered");       // fnp diagnostic jan 2023 
		double spread = states.nominalInterestRate - marketModel.stateAt(this.referenceRate,time,states,terms);
                if ( spread <= -0.045 ) { spread = -0.045 ; }  
System.out.println("****fnp011  spread=<" + String.valueOf(spread) + ">; starting age computation");       // fnp diagnostic jan 2023 	
	    	double age = dayCount.dayCountFraction(terms.<LocalDateTime>getAs("InitialExchangeDate"),states.statusDate);
System.out.println("****fnp012 age=<" + String.valueOf(age) + ">; starting surface lookup ");       // fnp diagnostic jan 2023  		
		return surface.getValueFor(spread,1).getValueFor(age,1);
	}
}

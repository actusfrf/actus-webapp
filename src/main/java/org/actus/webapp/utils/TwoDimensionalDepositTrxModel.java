package org.actus.webapp.utils;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.actus.states.StateSpace;
import org.actus.webapp.models.ObservedData;
import org.actus.webapp.models.TwoDimensionalDepositTrxModelData;
import org.actus.webapp.models.TwoDimensionalLabelSurfaceData;
import org.actus.attributes.ContractModelProvider;
// import org.actus.conventions.daycount.DayCountCalculator;
import org.actus.externals.RiskFactorModelProvider;

public class TwoDimensionalDepositTrxModel implements RiskFactorModelProvider {
	String riskFactorId;
	TimeSeries<Double,TimeSeries<Double,Double>> surface;
//	DayCountCalculator dayCount;
        HashMap<String,Double> dimension1Index, dimension2Index ;
	
	public TwoDimensionalDepositTrxModel() {
	}

	public TwoDimensionalDepositTrxModel(String riskFactorId, TwoDimensionalDepositTrxModelData data ) {
		this.riskFactorId = riskFactorId;
//		this.dayCount = new DayCountCalculator("A360", null);
		this.surface = new TimeSeries<Double,TimeSeries<Double,Double>>();
                this.dimension1Index = new HashMap<String,Double>();
                this.dimension2Index = new HashMap<String,Double>();

                // arrays of labels first 
         	String[] dim1Labels = data.getLabelSurface().getLabelMargins().get(0).getValues().stream().map(obs->obs).toArray(String[]::new);
		String[] dim2Labels = data.getLabelSurface().getLabelMargins().get(1).getValues().stream().map(obs->obs).toArray(String[]::new);
                // for each dimension pupulate hash map and build list/array of numeric lookup margin value 
		List<Double> dimension1Margins = new ArrayList<>();
                for(int i=0; i < dim1Labels.length ; i++ ) {
     		   this.dimension1Index.put(dim1Labels[i],Double.valueOf(i));
                   dimension1Margins.add(Double.valueOf(i));
                   }
                Double[ ] dimension2Margins = new Double[dim2Labels.length] ;
                for(int i=0; i < dim2Labels.length ; i++ ) {
                   this.dimension2Index.put(dim2Labels[i],Double.valueOf(i));
                   dimension2Margins[i] = Double.valueOf(i) ;
                   }
System.out.println("****fnp029 lookup date(0)= <" + dim2Labels[0] + "> .");   // fnp diagnostic jan 2023 
	
	//	List<Double> dimension1Margins = data.getSurface().getMargins().get(0).getValues();
	//	Double[] dimension2Margins = data.getSurface().getMargins().get(1).getValues().stream().map(obs -> obs).toArray(Double[]::new);
	
		List<List<Double>> values = data.getLabelSurface().getData();
		for(int i=0; i < values.size(); i++) {
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
                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                String timeS = time.format(formatter);
System.out.println("****fnp020 TwoDimensionalDepositTrxModel stateAt() entered");       // fnp diagnostic jan 2023 
                String contractID  = terms.getAs("ContractID");
// System.out.println("****fnp021 contractID = <" + contractID + "> time = <"+ String.valueOf(time)+">" ); // fnp diagnostic jan 2023 

System.out.println("****fnp021 contractID = <" + contractID + "> time = <" + timeS + ">" ); // fnp diagnostic jan 2023 
                Double dim1x = this.dimension1Index.get(contractID);
System.out.println("****fnp022 dim1x = <" + String.valueOf(dim1x) + ">");       // fnp diagnostic jan 2023
		Double dim2x = this.dimension2Index.get(timeS); 

//               Double dim2x = this.dimension2Index.get(String.valueOf(time)); 

System.out.println("****fnp023 dim2x = <" + String.valueOf(dim2x) + ">");       // fnp diagnostic jan 2023
                Double trxAmt = surface.getValueFor(dim1x,1).getValueFor(dim2x,1);
System.out.println("****fnp024 trxAmt = <" + String.valueOf(trxAmt) + ">");       // fnp diagnostic jan 2023    
		return trxAmt;  // surface.getValueFor(spread,1).getValueFor(age,1);
	}
}

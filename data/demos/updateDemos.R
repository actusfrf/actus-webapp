library(jsonlite)
library(readr)

source_path <- "./jsonOld"
save_path <- "./json"

# risk factor template
#rf_template = list(marketObjectCode = "", base = 1, data = list( list(time="2015-01-01T00:00:00", value="0.4")))

# read data csv demo files
file_names <- list.files(path = source_path,recursive = TRUE)

for (file_name in file_names) {
    json = fromJSON(paste0(source_path,"/",file_name))
    
    # update json keys
#    json$contract = json$terms
#    json$terms = NULL
    
    # update term names
#    json$contract$scalingIndexAtContractDealDate = json$contract$scalingIndexAtStatusDate
#    json$contract$scalingIndexAtStatusDate = NULL
    
    # add interest cycles to amortizing products
    if(!is.null(json$contract$cycleAnchorDateOfPrincipalRedemption) && is.null(json$contract$cycleAnchorDateOfInterestPayment)) {
        json$contract$cycleAnchorDateOfInterestPayment = json$contract$cycleAnchorDateOfPrincipalRedemption
    }
    if(!is.null(json$contract$cycleOfPrincipalRedemption) && is.null(json$contract$cycleOfInterestPayment)) {
        json$contract$cycleOfInterestPayment = json$contract$cycleOfPrincipalRedemption
    }
    
    # add riskfactor data
    # riskFactors = list()
    # if(!is.null(json$contract$marketObjectCodeRateReset)) {
    #     json$contract$marketObjectCodeOfRateReset = json$contract$marketObjectCodeRateReset
    #     json$contract$marketObjectCodeRateReset = NULL
    #     factor = rf_template
    #     factor$marketObjectCode = json$contract$marketObjectCodeOfRateReset
    #     factor$data = list( list(time=json$contract$statusDate, value=json$contract$nominalInterestRate) )
    #     riskFactors = c(riskFactors, list(factor))
    # }
    # if(!is.null(json$contract$marketObjectCodeOfScalingIndex)) {
    #     factor = rf_template
    #     factor$marketObjectCode = json$contract$marketObjectCodeOfScalingIndex
    #     factor$data = list( list(time=json$contract$statusDate, value=json$contract$scalingIndexAtContractDealDate) )
    #     riskFactors = c(riskFactors, list(factor))
    # }
    # json$riskFactors = riskFactors

    write_lines(toJSON(json,auto_unbox=TRUE), paste0(save_path, "/", file_name))
}

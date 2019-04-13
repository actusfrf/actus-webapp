
# global options
options(stringsAsFactors = FALSE)

# load libraries
library(jsonlite)
library(magrittr)
library(readr)

# define paths
forms_path <- '../data/forms/json/'
data_path <- '../data/forms/'

# define contracts to create forms for
build_contracts <- c("CSH","UMP","CLM","PAM","LAM","LAX","ANN","NAM","STK","COM","CEG","CEC","SWPPV","SWAPS","FXOUT","FUTUR","OPTNS","CAPFL")


# read data dictionary
rawdat <- read_csv(paste0(data_path,"Consolidated DD CTD v1.0.csv"), skip=1)

# convert enum values to json array
rawdat$'Allowed Values' <- sapply(sapply(sapply(rawdat$'Allowed Values',strsplit,"\n"),strsplit,"="),function(x) paste0("[",paste(sapply(x,function(y) trimws(y[1])),collapse=","),"]"))

# define json base form
base_form = data.frame(Identifier = "<Identifier>",
                       ContractType = "<Contract Type>",
                       Description = "TODO: Contract description here!",
                       Version = format(Sys.Date(), format = "%Y%m%d"),
                       Terms = "<Terms>")

# create forms by contract
for(ct in build_contracts) {
	
	# prepare json form
	form = base_form
	form$Identifier = paste0("form_",ct)
	form$ContractType = ct
	jsonForm = toJSON(unbox(form), pretty=TRUE)

	# extract contract specific terms
	terms = rawdat[	which(!is.na(rawdat[[ct]])),
			c("Group Name", "ACTUS Name", "Data Format", "Allowed Values", "Attribute Description", ct) ]

	# update column names
    	colnames(terms) = c("Group", "Name", "Type", "List", "Description", "Applicability")

	# fix ContractType enum to ct
	terms[which(terms$Name=="ContractType"),"List"]="[PAM]"

	# combine form-json and terms-json
	jsonTerms = unbox(toJSON(terms, auto_unbox=TRUE,pretty=TRUE))
  	jsonForm = gsub("\"<Terms>\"", jsonTerms, jsonForm)
  
  	# export final form
	jsonForm %>% write_lines(paste0(forms_path,'form_',ct,'.json'))
}

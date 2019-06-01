
# global options
options(stringsAsFactors = FALSE)

# load libraries
library(jsonlite)
library(magrittr)
library(readr)

# define paths
forms_path = '../data/forms/json/'
data_path = '../data/forms/'

# define contracts to create forms for (contracts currently covered in the actus-core lib)
build_contracts = c("CSH","UMP","CLM","COM","STK","PAM","LAM","NAM","ANN","SWPPV","FXOUT")

# read data dictionary
rawdat = read_csv(paste0(data_path,"Consolidated DD CTD v1.0.csv"), skip=1)

# read contract meta information
meta = read_csv(paste0(data_path,"ACTUS Taxonomy.csv"))

# convert enum values to json array
rawdat$'Allowed Values' = sapply(sapply(sapply(sapply(rawdat$'Allowed Values',strsplit,"\n"),strsplit,"="),function(x) sapply(x,function(y) trimws(y[1]))),toJSON)

#sapply(sapply(sapply(rawdat$'Allowed Values',strsplit,"\n"),strsplit,"="),function(x) paste0("[",paste(sapply(x,function(y) trimws(y[1])),collapse=","),"]"))

# define json base form
base_form = data.frame(Identifier = "<Identifier>",
                       ContractType = "<Contract Type>",
		       Name = "<Full name>",
                       Description = "<Contract description>",
                       Version = format(Sys.Date(), format = "%Y%m%d"),
                       Terms = "<Terms>")

# create forms by contract
for(ct in build_contracts) {
	
	# prepare json form
	form = base_form
	form$Identifier = paste0("form_",ct)
	form$ContractType = ct
	form$Name = ifelse(ct%in%meta$ContractType,subset(meta,ContractType==ct)$Name,"")
	form$Description = ifelse(ct%in%meta$ContractType,subset(meta,ContractType==ct)$Description,"Please check https://actusfrf.org for a description")
	jsonForm = toJSON(unbox(form), pretty=TRUE)

	# extract contract specific terms
	terms = rawdat[	which(!is.na(rawdat[[ct]])),
			c("Group Name", "ACTUS Name", "Data Format", "Allowed Values", "Attribute Description", ct) ]

	# update column names
    	colnames(terms) = c("Group", "Name", "Type", "List", "Description", "Applicability")

	# fix ContractType enum to ct
	terms[which(terms$Name=="ContractType"),"List"]="[PAM]"
	
	# remove NA strings
	terms[which(terms$Type!="Enum"),"List"]=""
	
	# combine form-json and terms-json
	jsonTerms = unbox(toJSON(terms, auto_unbox=TRUE,pretty=TRUE))
  	jsonForm = gsub("\"<Terms>\"", jsonTerms, jsonForm)
  
	# clean up form
	jsonForm = gsub("\"[", "[", jsonForm,fixed=TRUE)
	jsonForm = gsub("]\"", "]", jsonForm,fixed=TRUE)
	jsonForm = gsub("\"\"", "", jsonForm,fixed=TRUE)

  	# export final form
	jsonForm %>% write_lines(paste0(forms_path,'form_',ct,'.json'))
}

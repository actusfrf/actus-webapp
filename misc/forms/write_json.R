
library(jsonlite)
library(magrittr)
library(readr)

# define paths
terms_path <- './forms/terms/'
forms_path <- './forms/forms/'

# read data dictionary
rawdat <- read_csv("forms/Consolidated DD CTD v1.0.csv", skip=1)

# which contract types covered?
basic_contract <- c("CSH", "UMP",	"PBN",	"CLM",	"PAM",	"LAM",	"LAX",	"ANN",	"ANX",	"NAM",	"NAX",	"STK",	"COM")
# for testing
# basic_contract <- c("CSH", "COM")
combined_contract <- c("TXS (Transactions)",	"CEG",	"CEC",	"CRL",	"SWPPV",	"SWAPS",	"FXOUT",	"FUTUR",	"OPTNS",	"CAPFL",	"BNDCP",	"BNDWR",	"SWPTN",	"CAXFL",	"SCXOP",	"FXXOP",	"SECUR",	"Margining")

covered_contract <- c("CSH", "UMP",	"CLM",	"PAM",	"LAM",	"LAX",	"ANN",	"NAM",	"STK",	"COM", 
                      "CEG",	"CEC",	"SWPPV",	"SWAPS",	"FXOUT",	"FUTUR",	"OPTNS",	"CAPFL")

# prepare
forms <- data.frame()

identifiers <- c()
contract_type <- c()
description <- c()
vers <- c()
ctr_terms <- c()
terms_names <- c("Contract", "Group", "Name", "Type", "List", "Description", "Applicability")

# extract list values (Enum types) as json arrays
list_values <- sapply(sapply(sapply(rawdat[,"Allowed Values"],strsplit,"\n"),strsplit,"="),function(x) paste0("[",paste(sapply(x,function(y) trimws(y[1])),collapse=","),"]"))

# for every basic contract
build_group <- covered_contract #combined_contract # basic_contract

for (ctr in build_group) {
  identifiers <- c(identifiers, paste('form', ctr, sep = '_'))
  contract_type <- c(contract_type, ctr)
  description <- c(description, 'descr')
  vers <- c(vers, format(Sys.Date(), format = "%Y%m%d"))
  
  terms <- rawdat[[ctr]]
  idx <- 0
  available_ctr_bool <- c()
  
  # create a boolean list for the terms of every contract
  for (term in terms){
    idx <- idx+1
    
    if (!is.na(term)){
      available_ctr_bool[idx] <- TRUE
    } else {
      available_ctr_bool[idx] <- FALSE
    }
  }
  
  # some contracts don't have terms(yet?)
  if(any(available_ctr_bool)){
    
    # prepare the new terms for every contract type
    new_forms <- data.frame(rep(ctr, length(rawdat[available_ctr_bool,"Group Name"])),
                            rawdat[available_ctr_bool,"Group Name"], 
                            rawdat[available_ctr_bool,"ACTUS Name"], 
                            rawdat[available_ctr_bool,"Data Format"],
                            "", 
                            rawdat[available_ctr_bool,"Attribute Description"],
                            rawdat[available_ctr_bool,ctr])
	# add column names
    colnames(new_forms) <- terms_names
	new_forms$List=as.character(new_forms$List)

	# add list-values for Enum types
	new_forms[which(rawdat[available_ctr_bool,"Data Format"]=="Enum"), "List"] = list_values[which(rawdat[available_ctr_bool,"Data Format"]=="Enum")]
    
	# fix ContractType enum to current ctr
	new_forms[which(new_forms$Name=="ContractType"),"List"]=paste0("[",ctr,"]")

    # append to existing
    forms <- rbind(forms, new_forms)
    ctr_terms <- c(ctr_terms, new_forms)
  }
  # if there aren't any terms there won't be anything appended
  
  
}

# create terms list JSON file for all contracts
for (ctr in build_group) {
  ctr_terms <- forms[forms$Contract == ctr,]
  ctr_terms$Contract <- NULL
  
  path = paste0(terms_path,ctr,'.json')
  
  ctr_terms %>% toJSON() %>% write_lines(path)
  
}

# if executed the edited forms_files will be overwritten!
for (ctr in build_group) {
  
  base_form <- data.frame(Identifier <- paste("form_","", sep = ctr),
                          ContractType <- ctr,
                          Description <- paste("General description of ","", sep = ctr),
                          Version <- format(Sys.Date(), format = "%Y%m%d"),
                          Terms <- "REPLACE ME"
  )
  colnames(base_form) <- c("Identifier", "ContractType", "Description", "Version", "Terms")
  
  # convert base form to jsont string
  base_form_json <- toJSON(base_form)
  base_form_json_string <- substring(base_form_json,2,nchar(base_form_json)-1)
  
  
  # combine form-json and terms-json
  terms_json <- toJSON(fromJSON(file(paste(terms_path,ctr,".json", sep = ""))))
  final_form <- gsub("\"REPLACE ME\"", terms_json, base_form_json_string)
  
  # export final form
  path = paste0(forms_path,'form_',ctr,'.json')
  final_form %>% write_lines(path)
}


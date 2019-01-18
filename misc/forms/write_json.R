
library(jsonlite)
library(magrittr)
library(readr)

# define paths
terms_path <- './forms/terms/'
forms_path <- './forms/forms/'

# read data dictionary
#library(xlsx)
#rawdat = read.xlsx("forms/Consolidated_DD_CTD_TechCommittee.xlsx", sheetName = "Consolidated DD CTD")
library(readxl)
rawdat <- read_excel("forms/Consolidated_DD_CTD_TechCommittee.xlsx", sheet = "Consolidated DD CTD", skip = 1)

# remove bottom rows because they are empty
rawdat <- rawdat[1:141,]

# added types, i was note sure how to classify as list or not
notes_file <- read_excel("forms/notes_Consolidated_DD_CTD_TechCommittee.xlsx", sheet = "Consolidated DD CTD", skip = 1)
list_type <- notes_file$Type
list_values <- notes_file

basic_contract <- c("CSH", "UMP",	"PBN",	"CLM",	"PAM",	"LAM",	"LAX",	"ANN",	"ANX",	"NAM",	"NAX",	"STK",	"COM")
# for testing
# basic_contract <- c("CSH", "COM")
combined_contract <- c("TXS (Transactions)",	"CEG",	"CEC",	"CRL",	"SWPPV",	"SWAPS",	"FXOUT",	"FUTUR",	"OPTNS",	"CAPFL",	"BNDCP",	"BNDWR",	"SWPTN",	"CAXFL",	"SCXOP",	"FXXOP",	"SECUR",	"Margining")

covered_contract <- c("CSH", "UMP",	"CLM",	"PAM",	"LAM",	"LAX",	"ANN",	"NAM",	"STK",	"COM", 
                      "CEG",	"CEC",	"SWPPV",	"SWAPS",	"FXOUT",	"FUTUR",	"OPTNS",	"CAPFL", "Margining")

# prepare
forms <- data.frame()

identifiers <- c()
contract_type <- c()
description <- c()
vers <- c()
ctr_terms <- c()
terms_names <- c("Contract", "Group", "Name", "Type", "List", "Description", "Applicability")

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
                            list_type[available_ctr_bool], 
                            rep("some list", length(list_type[available_ctr_bool])), 
                            rawdat[available_ctr_bool,"Attribute Description"],
                            rawdat[available_ctr_bool,ctr])
    
    
    colnames(new_forms) <- terms_names
    
    # append to existing
    forms <- rbind(forms, new_forms)
    ctr_terms <- c(ctr_terms, new_forms)
  }
  # if there aren't any terms there won't be anything appended
  
  
}


# extract required infos


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


# To Do
## define items of list -> parse or manual?

# write as json
# library(readr)
# library(jsonlite)
# library(magrittr)
# df %>% 
#     toJSON() %>%
#     write_lines(path)



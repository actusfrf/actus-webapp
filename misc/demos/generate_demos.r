
library(jsonlite)
library(magrittr)
library(readr)
library(readxl)

folder_path <- "demos/csv/"
save_location <- "demos/json/"

# read data dictionary
file_names <- list.files(path = folder_path)
#file_names <- c("ANN_TestBed.xls", "FUTUR_TestBed.xls")

# These values are going to be replaced by NA's so the JSON 
filter_values <- c("NOCALENDAR", "NULL", "SD", -999999999, "N")


for ( file_name in file_names ) {
  path <- paste(folder_path, file_name, sep = "")
  file <- read_excel(paste("demos/csv/",file_name, sep = ""), sheet = "Demo Cases", na = "NA")
  file_fields <- colnames(file)
  
  # create folder and path for saving the demos
  sub_dir_name <- unlist(strsplit(file_name, split = "_"))[1]
  save_path <- paste(save_location, sub_dir_name, sep = "")
  dir.create(file.path(save_path), showWarnings = FALSE)
  
  # clean up file df
  for (filter_value in filter_values) {
    file[file == filter_value] <- NA
  }
  
  keys <- c("Identifier", "Label", "ContractType", "Version", "Description", "Terms")
  
  # boolean matrix because NA key/values don't need to be included in the demo
  bool_vec<- c()
  for (row in file) {
    bool_vec <- c(bool_vec, !is.na(row))
  }
  bool_matrix <- matrix(data = bool_vec, nrow = length(bool_vec)/ncol(file), ncol = ncol(file))
  
  
  for(i in 1:nrow(file)){
    demo_name <- paste("demo_", tolower(sub_dir_name), file[i, "ContractID"], sep = "")
    
    demo_df <- data.frame(
      demo_name,                                                        # identifier
      paste("CT ", file[i,"ContractID"], ": ",substring(file[i, "Description"], 1, 50), sep = ""), # label
      file[i,"ContractType"],                                           # contract type
      format(Sys.Date(), format = "%Y%m%d"),                            # version
      file[i, "Description"],                                           # Description
      "REPLACE ME"                                                      # Replacement string
      )
    colnames(demo_df) <- keys
    
    # Removing leading and trailing brackets
    json_string <- toJSON(demo_df)
    json_string <- substring(json_string, 2, nchar(json_string) - 1)
    
    terms_keys <- colnames(file[bool_matrix[i,] == TRUE])
    terms_json_string <- toJSON(file[i,][bool_matrix[i,] == TRUE])
    terms_json_string <- substring(terms_json_string, 2, nchar(terms_json_string) - 1)
    
    final_demo <- gsub("\"REPLACE ME\"", terms_json_string, json_string)
    
    write_lines(final_demo, paste(save_path,"/", demo_name, ".json", sep = ""))
    
  }
  
  
}

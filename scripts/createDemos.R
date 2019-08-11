
library(jsonlite)
library(magrittr)
library(readr)
library(readxl)


terms_path <- "../data/forms/json/"
folder_path <- "../data/demos/csv/"
save_location <- "../data/demos/json/"

# format column names
tocamel=function(x,delim=" ") {
	s <- strsplit(x, split=delim,fixed=TRUE)
	sapply(s, function(y) {
		if (any(is.na(y))) {
		    y
		}
		else {
		    first <- substring(y, 1, 1)
		    first <- toupper(first)
		    first[1] <- tolower(first[1])
		    paste(first, substring(y, 2), sep = "", collapse = "")
		}
	    })
}

# read data csv demo files
file_names <- list.files(path = folder_path)
#file_names <- c("ANN_TestBed.xls", "FUTUR_TestBed.xls")

# These values are going to be replaced by NA's so the JSON 
filter_values <- c("NOCALENDAR", "NULL", "SD", -999999999, "N")

file_names <- c("ANN_TestBed.xls","FXOUT_TestBed.xls","LAM_TestBed.xls",
                "LAX_TestBed.xls","NAM_TestBed.xls","PAM_TestBed.xls",
                "STK_TestBed.xls")
  
  for ( file_name in file_names ) {
    path <- paste(folder_path, file_name, sep = "")
    file <- read_excel(paste0(folder_path,file_name), sheet = "Demo Cases", na = "NA")
    file_fields <- colnames(file)
    
    # create folder and path for saving the demos
    sub_dir_name <- unlist(strsplit(file_name, split = "_"))[1]
    save_path <- paste(save_location, sub_dir_name, sep = "")
    dir.create(file.path(save_path), showWarnings = FALSE)
    
    # clean up file df
    for (filter_value in filter_values) {
      file[file == filter_value] <- NA
    }
    
    keys <- c("identifier", "label", "contractType", "version", "description", "terms")
    
    # boolean matrix because NA key/values don't need to be included in the demo
    bool_vec<- c()
    for (row in file) {
      bool_vec <- c(bool_vec, !is.na(row))
    }
    bool_matrix <- matrix(data = bool_vec, nrow = length(bool_vec)/ncol(file), ncol = ncol(file))
    
    
    for(i in 1:nrow(file)){
      demo_name <- paste("demo_", tolower(sub_dir_name), file[i, "contractID"], sep = "")
      ct <- file[i,"contractType"] # extract contract type
      
      demo_df <- data.frame(
        demo_name,                                                        # identifier
        paste("CT ", file[i,"contractID"], ": ",substring(file[i, "description"], 1, 50), sep = ""), # label
        ct,                                                               # contract type
        format(Sys.Date(), format = "%Y%m%d"),                            # version
        file[i, "description"],                                           # Description
        "REPLACE ME"                                                      # Replacement string
      )
      colnames(demo_df) <- keys
      
      # Removing leading and trailing brackets
      json_string <- toJSON(demo_df)
      json_string <- substring(json_string, 2, nchar(json_string) - 1)
      
      # get applicable terms for ct
      terms_obj <- fromJSON(file(paste(terms_path,"form_",ct,".json", sep = "")))$Terms
      terms_applicable <- tocamel(terms_obj$Name)
      
      # extract applicable terms from terms in file
      terms_keys <- intersect(colnames(file[bool_matrix[i,] == TRUE]),terms_applicable)
      terms_dat <- file[i,terms_keys]
      
      # convert times format
      terms_dat[grep("[[:digit:]]{4}-[[:digit:]]{2}-[[:digit:]]{2}T[[:digit:]]{2}",terms_dat)]=
        paste0(terms_dat[grep("[[:digit:]]{4}-[[:digit:]]{2}-[[:digit:]]{2}T[[:digit:]]{2}",terms_dat)],":00:00")
      
      # convert terms to json
      terms_json_string <- toJSON(terms_dat)
      terms_json_string <- substring(terms_json_string, 2, nchar(terms_json_string) - 1)
      
      # combine demo-json and terms-json
      final_demo <- gsub("\"REPLACE ME\"", terms_json_string, json_string)
      
      write_lines(final_demo, paste(save_path,"/", demo_name, ".json", sep = ""))
      
    }
    
    
  }

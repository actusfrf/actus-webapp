

# read data dictionary
library(xlsx)
rawdat = read.xlsx("Consolidated_DD_CTD_TechCommittee.xlsx", sheetName = "Consolidated DD CTD")

# extract required infos

# todo

# write as json
library(readr)
library(jsonlite)
df %>% 
    toJSON() %>%
    write_lines(path)



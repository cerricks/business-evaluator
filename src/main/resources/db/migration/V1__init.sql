create table income_tax_rates 
(
  id bigint generated by default as identity,
  filing_status varchar(255),
  flat_tax double,
  tax_rate double,
  taxable_income_range_from double,
  taxable_income_range_to double,
  primary key (id)
);

create table input_categories 
(
  id bigint generated by default as identity,
  input_name varchar(255),
  input_source varchar(255),
  input_type varchar(255),
  additional_expense_ind boolean,
  default_loan_rate double,
  default_loan_term_length integer,
  default_loan_term_unit varchar(255),
  primary key (id)
);

insert into input_categories (input_name,input_source,input_type,additional_expense_ind,default_loan_rate,default_loan_term_length,default_loan_term_unit) values ('Asking Price','SYSTEM','CURRENCY',true,.06,5,'YEARS');
insert into input_categories (input_name,input_source,input_type,additional_expense_ind,default_loan_rate,default_loan_term_length,default_loan_term_unit) values ('Down Payment','SYSTEM','CURRENCY',true,.05,5,'YEARS');
insert into input_categories (input_name,input_source,input_type,additional_expense_ind,default_loan_rate,default_loan_term_length,default_loan_term_unit) values ('Original Cash Flow','SYSTEM','CURRENCY',false,null,null,null);
insert into input_categories (input_name,input_source,input_type,additional_expense_ind,default_loan_rate,default_loan_term_length,default_loan_term_unit) values ('FF&E','USER','CURRENCY',true,.09,25,'YEARS');
insert into input_categories (input_name,input_source,input_type,additional_expense_ind,default_loan_rate,default_loan_term_length,default_loan_term_unit) values ('Inventory','USER','CURRENCY',true,.03,1,'YEARS');
insert into input_categories (input_name,input_source,input_type,additional_expense_ind,default_loan_rate,default_loan_term_length,default_loan_term_unit) values ('Rent Security Deposit','USER','CURRENCY',true,.03,1,'YEARS');

insert into income_tax_rates (id,filing_status,taxable_income_range_from,taxable_income_range_to,flat_tax,tax_rate) values (1,'SINGLE',0,9275,0,.1);
insert into income_tax_rates (id,filing_status,taxable_income_range_from,taxable_income_range_to,flat_tax,tax_rate) values (2,'SINGLE',9276,37650,927.50,.15);
insert into income_tax_rates (id,filing_status,taxable_income_range_from,taxable_income_range_to,flat_tax,tax_rate) values (3,'SINGLE',37651,91150,5183.75,.25);
insert into income_tax_rates (id,filing_status,taxable_income_range_from,taxable_income_range_to,flat_tax,tax_rate) values (4,'SINGLE',91151,190150,18558.75,.28);
insert into income_tax_rates (id,filing_status,taxable_income_range_from,taxable_income_range_to,flat_tax,tax_rate) values (5,'SINGLE',190151,413350,46278.75,.33);
insert into income_tax_rates (id,filing_status,taxable_income_range_from,taxable_income_range_to,flat_tax,tax_rate) values (6,'SINGLE',413351,415050,119934.75,.35);
insert into income_tax_rates (id,filing_status,taxable_income_range_from,taxable_income_range_to,flat_tax,tax_rate) values (7,'SINGLE',415051,null,120529.75,.396);
insert into income_tax_rates (id,filing_status,taxable_income_range_from,taxable_income_range_to,flat_tax,tax_rate) values (8,'MARRIED_FILING_JOINTLY',0,18550,0,.1);
insert into income_tax_rates (id,filing_status,taxable_income_range_from,taxable_income_range_to,flat_tax,tax_rate) values (9,'MARRIED_FILING_JOINTLY',18551,75300,1855,.15);
insert into income_tax_rates (id,filing_status,taxable_income_range_from,taxable_income_range_to,flat_tax,tax_rate) values (10,'MARRIED_FILING_JOINTLY',75301,151900,10367.50,.25);
insert into income_tax_rates (id,filing_status,taxable_income_range_from,taxable_income_range_to,flat_tax,tax_rate) values (11,'MARRIED_FILING_JOINTLY',151901,231450,29517.50,.28);
insert into income_tax_rates (id,filing_status,taxable_income_range_from,taxable_income_range_to,flat_tax,tax_rate) values (12,'MARRIED_FILING_JOINTLY',231451,413350,51791.50,.33);
insert into income_tax_rates (id,filing_status,taxable_income_range_from,taxable_income_range_to,flat_tax,tax_rate) values (13,'MARRIED_FILING_JOINTLY',413351,466950,111818.50,.35);
insert into income_tax_rates (id,filing_status,taxable_income_range_from,taxable_income_range_to,flat_tax,tax_rate) values (14,'MARRIED_FILING_JOINTLY',466951,null,130578.50,.396);
insert into income_tax_rates (id,filing_status,taxable_income_range_from,taxable_income_range_to,flat_tax,tax_rate) values (15,'QUALIFYING_WIDOWER',0,18550,0,.1);
insert into income_tax_rates (id,filing_status,taxable_income_range_from,taxable_income_range_to,flat_tax,tax_rate) values (16,'QUALIFYING_WIDOWER',18551,75300,1855,.15);
insert into income_tax_rates (id,filing_status,taxable_income_range_from,taxable_income_range_to,flat_tax,tax_rate) values (17,'QUALIFYING_WIDOWER',75301,151900,10367.50,.25);
insert into income_tax_rates (id,filing_status,taxable_income_range_from,taxable_income_range_to,flat_tax,tax_rate) values (18,'QUALIFYING_WIDOWER',151901,231450,29517.50,.28);
insert into income_tax_rates (id,filing_status,taxable_income_range_from,taxable_income_range_to,flat_tax,tax_rate) values (19,'QUALIFYING_WIDOWER',231451,413350,51791.50,.33);
insert into income_tax_rates (id,filing_status,taxable_income_range_from,taxable_income_range_to,flat_tax,tax_rate) values (20,'QUALIFYING_WIDOWER',413351,466950,111818.50,.35);
insert into income_tax_rates (id,filing_status,taxable_income_range_from,taxable_income_range_to,flat_tax,tax_rate) values (21,'QUALIFYING_WIDOWER',466951,null,130578.50,.396);
insert into income_tax_rates (id,filing_status,taxable_income_range_from,taxable_income_range_to,flat_tax,tax_rate) values (22,'MARRIED_FILING_SEPARATELY',0,9275,0,.1);
insert into income_tax_rates (id,filing_status,taxable_income_range_from,taxable_income_range_to,flat_tax,tax_rate) values (23,'MARRIED_FILING_SEPARATELY',9276,37650,927.50,.15);
insert into income_tax_rates (id,filing_status,taxable_income_range_from,taxable_income_range_to,flat_tax,tax_rate) values (24,'MARRIED_FILING_SEPARATELY',37651,75950,5183.75,.25);
insert into income_tax_rates (id,filing_status,taxable_income_range_from,taxable_income_range_to,flat_tax,tax_rate) values (25,'MARRIED_FILING_SEPARATELY',75951,115725,14758.75,.28);
insert into income_tax_rates (id,filing_status,taxable_income_range_from,taxable_income_range_to,flat_tax,tax_rate) values (26,'MARRIED_FILING_SEPARATELY',115726,206675,25895.75,.33);
insert into income_tax_rates (id,filing_status,taxable_income_range_from,taxable_income_range_to,flat_tax,tax_rate) values (27,'MARRIED_FILING_SEPARATELY',206676,233475,55909.25,.35);
insert into income_tax_rates (id,filing_status,taxable_income_range_from,taxable_income_range_to,flat_tax,tax_rate) values (28,'MARRIED_FILING_SEPARATELY',233476,null,65289.25,.396);
insert into income_tax_rates (id,filing_status,taxable_income_range_from,taxable_income_range_to,flat_tax,tax_rate) values (29,'HEAD_OF_HOUSEHOLD',0,13250,0,.1);
insert into income_tax_rates (id,filing_status,taxable_income_range_from,taxable_income_range_to,flat_tax,tax_rate) values (30,'HEAD_OF_HOUSEHOLD',13251,50400,1325,.15);
insert into income_tax_rates (id,filing_status,taxable_income_range_from,taxable_income_range_to,flat_tax,tax_rate) values (31,'HEAD_OF_HOUSEHOLD',50401,130150,6897.50,.25);
insert into income_tax_rates (id,filing_status,taxable_income_range_from,taxable_income_range_to,flat_tax,tax_rate) values (32,'HEAD_OF_HOUSEHOLD',130151,210800,26835,.28);
insert into income_tax_rates (id,filing_status,taxable_income_range_from,taxable_income_range_to,flat_tax,tax_rate) values (33,'HEAD_OF_HOUSEHOLD',210801,413350,49417,.33);
insert into income_tax_rates (id,filing_status,taxable_income_range_from,taxable_income_range_to,flat_tax,tax_rate) values (34,'HEAD_OF_HOUSEHOLD',413351,441000,116258.50,.35);
insert into income_tax_rates (id,filing_status,taxable_income_range_from,taxable_income_range_to,flat_tax,tax_rate) values (35,'HEAD_OF_HOUSEHOLD',441001,null,125936,.396);
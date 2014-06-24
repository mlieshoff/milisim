create table studivz_user (
  id                        integer not null,
  name                      varchar(255),
  display_name              varchar(255),
  gender                    integer,
  constraint ck_studivz_user_gender check (gender in ('1','2')),
  constraint pk_studivz_user primary key (id))
;

create sequence studivz_user_seq;




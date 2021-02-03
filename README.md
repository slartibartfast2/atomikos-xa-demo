# atomikos-xa-demo

mysql ddls

```
CREATE TABLE `accounts` (
`id` bigint(20) NOT NULL,
`username` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `account_seq` (
`next_val` bigint(20) NOT NULL AUTO_INCREMENT,
PRIMARY KEY (`next_val`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
```

postgresql ddls

```
create table demo.accounts
(
	id numeric not null
		constraint accounts_pk
			primary key,
	username varchar(200) not null
);

alter table demo.accounts owner to postgres;

create sequence demo.account_seq;

alter sequence iyzipay.account_seq owner to postgres;
```
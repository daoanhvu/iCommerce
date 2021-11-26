create sequence hibernate_sequence start with 1 increment by 1;
create table cart (id bigint not null, order_date date, total_amount double not null, user_session_id varchar(255), primary key (id));
create table cart_item (id bigint not null, quantity integer not null, product_id bigint, primary key (id));
create table cart_items (cart_id bigint not null, items_id bigint not null);
create table products (id bigint not null, brand varchar(255), category varchar(255), colour varchar(255), name varchar(255), price double not null, primary key (id));
alter table cart_items add constraint UK_383kkp3af9dpn91t406oqe9n1 unique (items_id);
alter table cart_item add constraint FKqkqmvkmbtiaqn2nfqf25ymfs2 foreign key (product_id) references products;
alter table cart_items add constraint FKnqjva2t0na43f4qxm3xprl2qu foreign key (items_id) references cart_item;
alter table cart_items add constraint FK99e0am9jpriwxcm6is7xfedy3 foreign key (cart_id) references cart;


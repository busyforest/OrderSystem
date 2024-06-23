create table if not exists User(
    id int auto_increment,
    passwd varchar(20),
    avatar_path varchar(50),
    primary key (id)
);

create table if not exists message(
    sender_id int,
    receiver_id int,
    message varchar(50),
    message_time timestamp,
    primary key (sender_id, receiver_id, message_time),
    foreign key (sender_id) references User(id),
    foreign key (receiver_id) references User(id)
);

create table if not exists purchaser(
    id int,
    name varchar(20),
    gender varchar(1),
    studentIDOrWorkID int,
    foreign key (id) references User(id),
    primary key (id)
);

create table if not exists seller(
    id int,
    name varchar(20),
    brief_information varchar(50),
    address varchar(20),
    featured_dish varchar(20),
    avg_mark float,
    foreign key (id) references User(id),
    primary key (id)
);

create table if not exists administrator(
    id int,
    name varchar(20),
    foreign key (id) references User(id),
    primary key (id)
);

create table if not exists interact_seller(
    purchaser_id int,
    seller_id int,
    isFavorite boolean,
    comment varchar(100),
    mark int,
    primary key (purchaser_id, seller_id),
    foreign key (purchaser_id) references purchaser(id),
    foreign key (seller_id) references seller(id)
);

create table if not exists orderOverview(
    order_id int auto_increment,
    purchaser_id int,
    order_time varchar(20),
    dish_status varchar(10),
    primary key (order_id)
);


create table if not exists dish(
    dish_id int auto_increment,
    seller_id int,
    name varchar(20),
    price float,
    picture varchar(150),
    description varchar(50),
    ingredients varchar(50),
    nutrition_information varchar(50),
    possible_allergens varchar(50),
    avg_mark float,
    online_sales_volume int,
    offline_sales_volume int,
    primary key (dish_id),
    foreign key (seller_id) references seller(id)
);

create table if not exists order_dish(
    order_id int,
    dish_id int,
    quantity int,
    purchase_method varchar(10) check (purchase_method in ('排队点餐', '在线点餐')),
    comment varchar(50),
    mark int,
    primary key (order_id, dish_id),
    foreign key (order_id) references orderOverview(order_id),
    foreign key (dish_id) references dish(dish_id)
);



create table if not exists interact_dish(
    purchaser_id int,
    dish_id int,
    isFavorite boolean,
    primary key (purchaser_id, dish_id),
    foreign key (purchaser_id) references purchaser(id),
    foreign key (dish_id) references dish(dish_id)
);

create table if not exists dish_price_history(
    dish_id int,
    price float,
    time_stamp timestamp,
    primary key (dish_id, time_stamp),
    foreign key (dish_id) references dish(dish_id)
);

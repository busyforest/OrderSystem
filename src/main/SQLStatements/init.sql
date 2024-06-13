create table if not exists User(
    id int auto_increment,
    name varchar(20),
    passwd varchar(20),
    avatar_path varchar(50),
    primary key (id)
);

create table if not exists message(
    message_id int,
    user_id int,
    message varchar(50),
    message_time varchar(50),
    primary key (message_id),
    foreign key (user_id) references User(id)
);

create table if not exists purchaser(
    id int,
    gender varchar(1),
    studentIDOrWorkID int,
    foreign key (id) references User(id),
    primary key (id)
);

create table if not exists seller(
    id int,
    brief_information varchar(50),
    address varchar(20),
    featured_dish varchar(20),
    foreign key (id) references User(id),
    primary key (id)
);

create table if not exists administrator(
    id int,
    foreign key (id) references User(id),
    primary key (id)
);

create table if not exists interact_shop(
    purchaser_id int,
    seller_id int,
    comment varchar(60),
    isFavorite boolean,
    primary key (purchaser_id, seller_id),
    foreign key (purchaser_id) references purchaser(id),
    foreign key (seller_id) references seller(id)
);

create table if not exists orderOverview(
    order_id int,
    order_time varchar(20),
    dish_status varchar(10),
    primary key (order_id)
);

create table if not exists purchase_order(
    order_id int,
    user_id int,
    primary key (order_id),
    primary key (user_id),
    foreign key (user_id) references purchaser(id),
    foreign key (order_id) references orderOverview(order_id)
);

create table if not exists dish(
    dish_id int,
    seller_id int,
    name varchar(20),
    price int,
    picture varchar(50),
    description varchar(50),
    ingredients varchar(50),
    nutrition_information varchar(50),
    possible_allergens varchar(50),
    primary key (dish_id),
    foreign key (seller_id) references seller(id)
);

create table if not exists order_dish(
    order_id int,
    dish_id int,
    primary key (order_id),
    primary key (dish_id),
    foreign key (order_id) references orderOverview(order_id),
    foreign key (dish_id) references dish(dish_id)
);



create table if not exists interact_dish(
    purchaser_id int,
    dish_id int,
    comment varchar(60),
    isFavorite boolean,
    primary key (purchaser_id, dish_id),
    foreign key (purchaser_id) references purchaser(id),
    foreign key (dish_id) references dish(dish_id)
);



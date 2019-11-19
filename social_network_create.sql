create table friendship_statuses
(
    id     int auto_increment
        primary key,
    status varchar(50) not null
);

create table users
(
    id                  int auto_increment
        primary key,
    username            varchar(50)       not null,
    password            varchar(68)       not null,
    enabled             tinyint default 1 not null,
    email               varchar(50)       null,
    user_photo          longblob          null,
    cover_photo         longblob          null,
    userPhotoVisibility tinyint default 1 not null,
    first_name          varchar(50)       null,
    last_name           varchar(50)       null,
    create_date         timestamp         null,
    update_date         timestamp         null,
    nameVisibility      tinyint default 1 not null,
    constraint user_email_uindex
        unique (email),
    constraint user_username_uindex
        unique (username)
);

create table authorities
(
    username  varchar(50) not null,
    authority varchar(50) not null,
    constraint username_authority
        unique (username, authority),
    constraint FK__users
        foreign key (username) references users (username)
);

create table friendships
(
    id                   int auto_increment
        primary key,
    friendship_status_id int       default 1                   not null,
    create_date          timestamp default current_timestamp() not null,
    update_date          timestamp default current_timestamp() not null,
    enabled              tinyint   default 1                   not null,
    user_id              int                                   not null,
    friend_id            int                                   not null,
    constraint friendships_user_id_friend_id_uindex
        unique (user_id, friend_id),
    constraint friendships_friendship_statuses_id_fk
        foreign key (friendship_status_id) references friendship_statuses (id),
    constraint friendships_users_id_fk
        foreign key (user_id) references users (id),
    constraint friendships_users_id_fk_2
        foreign key (friend_id) references users (id)
);

create table posts
(
    id          int auto_increment
        primary key,
    content     varchar(255)                          not null,
    photo       longblob                              null,
    user_id     int                                   not null,
    video       varchar(255)                          null,
    create_date timestamp default current_timestamp() not null on update current_timestamp(),
    visibility  tinyint   default 1                   not null,
    enabled     tinyint   default 1                   not null,
    location    varchar(255)                          null,
    update_date timestamp default current_timestamp() not null,
    constraint posts_content_uindex
        unique (content),
    constraint posts_users_id_fk
        foreign key (user_id) references users (id)
);

create table comments
(
    id          int auto_increment
        primary key,
    content     varchar(255)                          not null,
    create_date timestamp default current_timestamp() not null,
    update_date timestamp default current_timestamp() not null,
    enabled     tinyint   default 1                   not null,
    user_id     int                                   not null,
    post_id     int                                   not null,
    parent_id   int                                   null,
    constraint comment_comment_id_fk
        foreign key (parent_id) references comments (id),
    constraint comment_posts_id_fk
        foreign key (post_id) references posts (id),
    constraint comment_users_id_fk
        foreign key (user_id) references users (id)
);

create table liked_comments
(
    id         int auto_increment
        primary key,
    user_id    int               null,
    comment_id int               null,
    enabled    tinyint default 1 null,
    constraint liked_comments_comment_id_fk
        foreign key (comment_id) references comments (id),
    constraint liked_comments_users_id_fk
        foreign key (user_id) references users (id)
);

create table liked_posts
(
    id      int auto_increment
        primary key,
    user_id int               not null,
    post_id int               not null,
    enabled tinyint default 1 not null,
    constraint liked_posts_posts_id_fk
        foreign key (post_id) references posts (id),
    constraint liked_posts_users_id_fk
        foreign key (user_id) references users (id)
);

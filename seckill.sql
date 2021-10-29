create table t_user(
	id bigint(18) not null comment '手机号作为id',
	nickname varchar(255) not null comment '用户名',
	password varchar(32) default null comment '密码',
	salt varchar(10) default null comment '加密盐',
	head varchar(128) default null comment '头像',
	register_date datetime default null comment '注册日期',
	last_login_date datetime default null comment '最后登录时间',
	login_count int default '0' comment '登录次数',
	primary key(id)
)engine = innodb auto_increment=3 default charset = utf8mb4;

create table t_goods(
	id bigint(20) not null auto_increment comment '商品id',
	goods_name varchar(16) default null comment '商品名称',
	goods_title varchar(64) default null comment '商品标题',
	goods_img varchar(64) default null comment '商品图片',
	goods_detail longtext comment '商品详情',
	goods_price decimal(10,2) default '0.00' comment '商品价格',
	goods_stock int default '0' comment "商品库存，-1没有限制",
	primary key(id)
)engine = innodb auto_increment=3 default charset = utf8mb4;

create table t_order(
	id bigint(20) not null auto_increment comment '订单id',
	user_id bigint(20) default null comment '用户id',
	goods_id bigint(20) default null comment '商品id',
	delivery_addr_id bigint(20) default null comment '收货地址id',
	goods_name varchar(16) default null comment '冗余的商品名称',
	goods_count int default '0' comment '商品数量',
	goods_price decimal(10,2) default '0.00' comment '冗余商品单价',
	order_channel tinyint(4) default '0' comment '下单渠道 1pc，2android，3ios',
	status tinyint(4) default '0' comment '订单状态 0新建未支付，1已支付，2已发货，3已收货，4已退款，5已完成',
	create_date datetime default null comment '订单创建时间',
	pay_date datetime default null comment '订单支付时间',
	primary key(id)
)engine = innodb auto_increment=12 default charset = utf8mb4;

create table t_seckill_goods(
	id bigint(20) not null auto_increment comment '秒杀商品id',
	goods_id bigint(20) default null comment '商品id',
	seckill_price decimal(10,2) default '0.00' comment '秒杀价格',
	stock_count int(10) default null comment '库存数量',
	start_date datetime default null comment '开始时间',
	end_date datetime default null comment '结束时间',
	primary key(id)
)engine = innodb auto_increment=12 default charset = utf8mb4;

create table t_seckill_order(
	id bigint(20) not null auto_increment comment '秒杀订单id',
	user_id bigint(20) default null comment '用户id',
	order_id bigint(20) default null comment '订单id',
	goods_id bigint(20) default null comment '商品id',
	primary key(id)
)engine = innodb auto_increment=3 default charset = utf8mb4;

insert into t_goods values(1,'iphone 12','iphone 12 64GB','/img/iphone12.png','iphone 12 64GB','6299.00',100);
insert into t_goods values(2,'iphone 12 PRO','iphone 12 PRO 128GB','/img/iphone12pro.png','iphone 12 64GB','9299.00',100);
insert into t_seckill_goods values(1,1,'629',10,'2020-11-01 08:00:00','2020-11-01 09:00:00');
insert into t_seckill_goods values(1,2,'929',10,'2020-11-01 08:00:00','2020-11-01 09:00:00');

-- -----------------------------------------------------
-- Schema commodity
-- -----------------------------------------------------

create schema commodity collate utf8mb4_general_ci;

create table if not exists commodity.commodity_backend_category
(
    id            bigint auto_increment
        primary key,
    category_code varchar(50)       not null comment '类目编码',
    category_name varchar(50)       not null comment '类目名称',
    level         tinyint           not null comment '类目层级',
    path          varchar(300)      not null comment '类路径',
    create_time   bigint            not null,
    update_time   bigint            not null,
    status        tinyint default 1 not null comment '前台类目是否可用(1:可用,0:禁用)',
    enabled       tinyint           not null comment '数据是否可用(1:可用,0:不可用(逻辑删除)  ) ',
    constraint category_code_UNIQUE
        unique (category_code),
    constraint path_UNIQUE
        unique (path)
)
    comment '商品前台类目';

create table if not exists commodity.commodity_brand
(
    id          int auto_increment
        primary key,
    brand_code  varchar(50)       not null comment '品牌编码',
    brand_name  varchar(50)       not null comment '品牌名称',
    enabled     tinyint default 1 not null comment '数据是否可用(1:可用,0:不可用(逻辑删除)  ) ',
    create_time bigint            not null,
    update_time bigint            not null,
    constraint brande_code_UNIQUE
        unique (brand_code)
)
    comment '品牌';

create table if not exists commodity.commodity_description
(
    id           bigint       not null
        primary key,
    image_url    varchar(150) null comment '商品图片',
    sku_code     varchar(45)  not null comment 'sku code',
    introduction text         not null comment '商品介绍',
    create_time  bigint       not null,
    update_time  bigint       not null,
    enabled      tinyint      not null comment '数据是否可用(1:可用,0:不可用(逻辑删除)  ) '
)
    comment '商品文描';

create table if not exists commodity.commodity_frontend_category
(
    id            bigint auto_increment
        primary key,
    category_code varchar(50)       not null comment '类目编码',
    category_name varchar(50)       not null comment '类目名称',
    level         tinyint           not null comment '类目层级',
    path          varchar(300)      not null comment '类路径',
    create_time   bigint            not null,
    update_time   bigint            not null,
    status        tinyint default 1 not null comment '前台类目是否可用(1:可用,0:禁用)',
    enabled       tinyint           not null comment '数据是否可用(1:可用,0:不可用(逻辑删除)  ) ',
    constraint category_code_UNIQUE
        unique (category_code),
    constraint path_UNIQUE
        unique (path)
)
    comment '商品前台类目';

create table if not exists commodity.commodity_price
(
    id            bigint            not null
        primary key,
    create_time   bigint            not null,
    update_time   bigint            not null,
    sku_code      varchar(50)       not null comment 'sku编码',
    price         varchar(50)       not null comment '价格',
    currency      varchar(20)       not null comment '币种(人民币-CNY..)',
    regional_code varchar(50)       not null comment '区域编码(中国-CN…)',
    enabled       tinyint default 1 not null comment '数据是否可用(1:可用,0:不可用(逻辑删除)  ) '
);

create table if not exists commodity.commodity_property
(
    id            bigint auto_increment
        primary key,
    property_code varchar(50)       not null comment '属性编码',
    property_name varchar(45)       not null comment '属性名称',
    property_type tinyint           not null comment '属性类型(1:公共属性,2关键属性,3销售属性)',
    status        tinyint default 1 not null comment '属性是否启用(1:启用,0:停用)',
    update_time   bigint            not null,
    create_time   bigint            not null,
    enabled       tinyint default 1 not null comment '数据是否可用(1:可用,0:不可用(逻辑删除)  ) ',
    constraint property_code_UNIQUE
        unique (property_code)
)
    comment '商品属性';

create table if not exists commodity.commodity_property_group
(
    id            bigint auto_increment
        primary key,
    property_code varchar(45)       not null comment '属性编码',
    group_id      varchar(45)       not null,
    update_time   bigint            not null,
    create_time   bigint            not null,
    status        tinyint default 1 not null comment '属性组是否可用(1:可用,0禁用)',
    enabled       tinyint default 1 not null comment '数据是否可用(1:可用,0:不可用(逻辑删除)  ) '
)
    comment '商品属性组';

create table if not exists commodity.commodity_property_value
(
    id             bigint auto_increment
        primary key,
    create_time    bigint       not null,
    update_time    bigint       not null,
    property_code  varchar(50)  not null,
    property_value varchar(500) not null comment '属性值',
    enabled        tinyint      null comment '数据是否可用(1:可用,0:不可用(逻辑删除)  ) '
)
    comment '商品属性';

create table if not exists commodity.commodity_putaway_manage
(
    id            bigint auto_increment
        primary key,
    update_time   bigint            not null,
    create_time   bigint            not null,
    putaway_staus tinyint default 1 not null comment '上下架状态(1:上架,0下架)',
    sku_code      varchar(50)       not null,
    putway_time   bigint            not null comment '上架时间',
    enabled       tinyint default 1 not null comment '数据是否可用(1:可用,0:不可用(逻辑删除)  ) '
)
    comment '商品上下架管理';

create table if not exists commodity.commodity_sku
(
    id           bigint            not null
        primary key,
    sku_code     varchar(50)       not null comment 'sku 编码',
    sku_name     varchar(100)      not null comment 'sku名称',
    spu_code     varchar(50)       not null comment 'spu code',
    retail_price varchar(50)       not null comment '建议零售价',
    cost_price   varchar(50)       not null comment '成本价',
    tax          varchar(50)       not null comment '税率',
    unit         varchar(20)       null comment '单位',
    bar_code     varchar(45)       null comment '条形码',
    status       tinyint default 1 not null comment 'sku 状态(1:可用,0:停用)',
    create_time  bigint            not null comment '创建时间',
    update_time  bigint            not null comment '更新时间',
    enabled      tinyint default 1 not null comment '数据是否可用(1:可用,0:不可用(逻辑删除)  ) ',
    constraint sku_code_UNIQUE
        unique (sku_code)
)
    comment 'sku表  库存量单位。是对每一个产品和服务的唯一标示符，例如iPhone X 128G 银色就是一个SKU，该系统的使用SKU的值进行数据管理，使公司能够跟踪管理，如仓库商品的库存情况。SKU需要有独立的条形码，方便仓库进行统计管理等。';

create table if not exists commodity.commodity_spu
(
    id                    bigint            not null
        primary key,
    spu_code              varchar(50)       not null comment 'spu 编码',
    spu_name              varchar(50)       not null comment 'spu 名称',
    brand_code            varchar(50)       not null comment '品牌编码',
    backend_category_code varchar(50)       not null comment '后台类目编码',
    status                tinyint           not null comment 'Spu是否启用(1:启用,0:停用)',
    create_time           bigint            not null comment '创建时间',
    update_time           bigint            not null comment '修改时间',
    enabled               tinyint default 1 not null comment '数据是否可用(1:可用,0:不可用(逻辑删除)  ) ',
    constraint spu_code_UNIQUE
        unique (spu_code)
)
    comment 'spu表';

create table if not exists commodity.sku_property_mapping
(
    id             bigint            not null
        primary key,
    sku_code       varchar(50)       not null comment 'sku编码',
    property_code  varchar(45)       not null comment '属性编码',
    property_value varchar(500)      not null comment '属性值',
    property_type  tinyint           not null comment '属性类型(1:公共属性,2关键属性,3销售属性),这里做个冗余,避免查商品属性时还要关联属性表查询属性类型',
    create_time    bigint            not null,
    update_time    bigint            not null,
    enabled        tinyint default 1 not null comment '数据是否可用(1:可用,0:不可用(逻辑删除)  ) '
)
    comment ' sku属性映射表';

create table if not exists commodity.spu_frontend_category_mapping
(
    id                     bigint            not null
        primary key,
    spu_code               varchar(50)       not null comment 'sku编码',
    frontend_category_code varchar(50)       not null comment '前台类目编码',
    create_time            bigint            not null,
    update_time            bigint            not null,
    enabled                tinyint default 1 not null comment '数据是否可用(1:可用,0:不可用(逻辑删除)  ) '
)
    comment 'spu前台类目映射';

create table if not exists commodity.spu_property_mapping
(
    id             bigint            not null
        primary key,
    spu_code       varchar(50)       not null comment 'sku编码',
    property_code  varchar(45)       not null comment '属性编码',
    property_value varchar(500)      not null comment '属性值',
    property_type  tinyint           not null comment '属性类型(1:公共属性,2关键属性,3销售属性),这里做个冗余,避免查商品属性时还要关联属性表查询属性类型',
    create_time    bigint            not null,
    update_time    bigint            not null,
    enabled        tinyint default 1 not null comment '数据是否可用(1:可用,0:不可用(逻辑删除)  ) '
)
    comment ' sku属性映射表';

-- -----------------------------------------------------
-- Schema member
-- -----------------------------------------------------
create schema member collate utf8mb4_general_ci;

create table if not exists member.authentication_type
(
    id                       bigint auto_increment
        primary key,
    authentication_type_code varchar(50)       not null comment '认证类型编码',
    authentication_type_name varchar(50)       not null comment '认证类型名称',
    enabled                  tinyint default 1 not null comment '是否逻辑可用(1:是,0:否)'
)
    comment '认证类型枚举表';

create table if not exists member.business_type
(
    id                 bigint auto_increment
        primary key,
    business_type_name varchar(45)       not null,
    business_type_code varchar(45)       not null,
    enabled            tinyint default 1 not null comment '是否逻辑可用(1:是,0:否)'
)
    comment '业务类型枚举表';

create table if not exists member.member_authentication_info
(
    id                       int auto_increment
        primary key,
    authentication_type_code varchar(45)       not null,
    authentication_key       varchar(100)      not null comment '认证的key(如果是账户认证,这里就是昵称)',
    authentication_value     varchar(200)      not null comment '认证值(如果是账户登录,这里就是账户密码)',
    member_id                bigint            not null comment '会员id',
    create_time              bigint            not null,
    update_time              bigint            not null,
    enabled                  tinyint default 1 not null comment '是否逻辑可用(1:是,0:否)'
)
    comment '会员认证信息';

create table if not exists member.member_delivery_info
(
    id              bigint            not null
        primary key,
    member_id       bigint            not null comment '会员id',
    address         varchar(300)      not null comment '地址',
    default_address tinyint default 0 not null comment '是否是默认地址(1:默认地址,0:非默认地址)',
    receiver_phone  bigint            not null comment '收货人电话',
    receiver_name   varchar(30)       not null comment '收货人姓名',
    create_time     bigint            not null,
    update_time     bigint            not null,
    enabled         tinyint default 1 not null comment '数据是否可用(1:可用,0:不可用(逻辑删除)  ) '
)
    comment '会员下单地址表';

create index member_id_index
    on member.member_delivery_info (member_id);

create table if not exists member.member_growth_change_history
(
    id                  bigint            not null comment '主键,数据量大,考虑分库分表'
        primary key,
    member_growth_value int               not null comment '成长值',
    member_id           bigint            not null comment '会员id',
    business_type_code  varchar(50)       not null comment '业务类型编码',
    business_id         varchar(50)       not null comment '业务id(活动id,订单id)',
    create_time         bigint            not null,
    update_time         bigint            not null,
    enabled             tinyint default 1 not null comment '是否逻辑可用(1:是,0:否)'
)
    comment '会员成长值变化历史记录表';

create table if not exists member.member_info
(
    id           bigint            not null comment '主键,考虑分布式场景,不使用自增主键'
        primary key,
    nickname     varchar(100)      not null comment '昵称',
    member_id    bigint            not null comment '会员id',
    sex          tinyint           not null comment '性别',
    age          int               not null comment '年龄',
    account      varchar(100)      not null comment '账号',
    score        int     default 0 not null comment '会员积分',
    growth_value int     default 0 not null comment '成长值,根据成长值计算会员等级',
    create_time  bigint            not null comment '创建时间',
    update_time  bigint            not null comment '修改时间',
    birthday     bigint            null comment '生日',
    enabled      tinyint default 1 not null,
    constraint account_UNIQUE
        unique (account),
    constraint member_id_UNIQUE
        unique (member_id),
    constraint nickname_UNIQUE
        unique (nickname)
)
    comment '会员基础信息表';

create table if not exists member.member_level
(
    id              bigint auto_increment
        primary key,
    level_code      varchar(45)       not null comment '等级编码',
    level_name      varchar(45)       not null comment '等级名称',
    level_threshold int     default 0 not null comment '等级门槛成长值',
    enabled         tinyint default 1 not null comment '是否逻辑可用(1:是,0:否)',
    constraint level_code_UNIQUE
        unique (level_code),
    constraint level_name_UNIQUE
        unique (level_name)
)
    comment '会员等级枚举表';

create table if not exists member.member_login_history
(
    id                       bigint            not null
        primary key,
    member_id                varchar(45)       not null,
    login_time               bigint            not null,
    login_ip                 varchar(45)       not null,
    authentication_type_code varchar(45)       not null comment '认证类型编码(微信,qq,邮箱,账号)',
    enabled                  tinyint default 1 not null comment '是否逻辑可用(1:是,0:否)'
)
    comment '会员登录历史表';

create table if not exists member.member_score_change_history
(
    id                 bigint            not null comment '主键(数据量会较大,考虑分布式场景,不使用自增)'
        primary key,
    member_id          bigint            not null comment '会员id',
    score              int               not null comment '变更积分值',
    business_type_code varchar(50)       not null comment '业务类型编码(活动,订单等)',
    business_id        varchar(50)       not null comment '业务id(订单id,活动id等)',
    score_type_code    varchar(50)       not null comment '积分类型编码(永久的,有使用期限的等)',
    `expire time`      bigint            null comment '如果积分类型存在过期时间,需要记录过期时间',
    create_time        bigint            not null,
    update_time        bigint            not null,
    enabled            tinyint default 1 not null comment '是否逻辑可用(1:是,0:否)'
)
    comment '会员积分变更历史表';

create table if not exists member.score_type
(
    id              bigint auto_increment
        primary key,
    score_type_code varchar(50) not null comment '积分类型编码',
    score_type_name varchar(50) not null comment '积分类型名称',
    enabled         tinyint as (1) comment '是否逻辑可用(1:是,0:否)'
)
    comment '积分类型枚举表';


-- -----------------------------------------------------
-- Schema order
-- -----------------------------------------------------
create schema `order` collate utf8_general_ci;

create table if not exists `order`.`order`
(
    id              bigint                     not null
        primary key,
    order_id        bigint                     not null comment '订单id',
    buyer_member_id bigint                     not null comment '买家会员id',
    parent_order_id varchar(100)               null comment '父订单id',
    order_amount    varchar(50)                not null comment '订单金额',
    pay_amount      varchar(50)                not null comment '支付金额
',
    discount_amount varchar(50) default '0.00' null comment '优惠金额',
    tax_amount      varchar(50) default '0.00' null comment '税费',
    score_deduction varchar(50) default '0.00' null comment '积分抵扣',
    score           int         default 0      null comment '赠送积分',
    growth_value    int         default 0      null comment '成长值',
    status          tinyint     default 0      not null comment '-2:超时取消,-1:用户取消,0:待支付,1:待发货,2:待收货,3:交易完成,4:待退款,5:仅退款,6:退货退款',
    receive_address varchar(300)               not null comment '收货地址',
    receiver_name   varchar(50)                not null comment '收货人姓名',
    receiver_phone  bigint                     not null comment '收货人电话',
    create_time     bigint                     not null comment '订单创建时间',
    update_time     bigint                     not null comment '订单修改时间',
    enabled         tinyint                    null comment '数据是否可用(1:可用,0:不可用(逻辑删除))',
    currency        varchar(20) default 'CNY'  not null comment '币种',
    constraint order_order_id_uindex
        unique (order_id)
)
    comment '订单表(正向)';

create table if not exists `order`.order_activity
(
    id              bigint      null,
    order_id        bigint      not null comment '订单id',
    activity_id     bigint      not null comment '活动id',
    discount_amount varchar(50) null comment '折扣金额',
    exchange_score  int         null comment '赠送积分',
    enabled         tinyint     null comment '数据是否可用(1:可用,0:不可用(逻辑删除)  )',
    create_time     bigint      not null comment '创建时间',
    update_time     bigint      not null comment '修改时间',
    currency        varchar(20) not null comment '币种'
)
    comment '订单活动表';

create table if not exists `order`.order_article_code
(
    id                bigint                     not null
        primary key,
    order_id          bigint                     not null comment '订单id',
    sku_code          varchar(50)                not null comment 'sku 编码',
    share_price       bigint                     not null comment '分摊金额',
    article_code      varchar(100)               not null comment '物品码',
    tax_amount        varchar(50) default '0.00' null comment '税费',
    article_code_type tinyint                    null comment '物品码类型(0:imei,1:sn)',
    enabled           tinyint     default 1      not null comment '数据是否可用(1:可用,0:不可用(逻辑删除))',
    create_time       bigint                     not null comment '创建时间',
    update_time       bigint                     null comment '更新时间',
    currency          varchar(20) default 'CNY'  not null
)
    comment '订单物品码表';

create table if not exists `order`.order_detail
(
    id                  bigint                    not null
        primary key,
    order_id            bigint                    not null comment '订单id',
    sku_code            varchar(50)               not null comment 'sku 编码',
    quantity            int                       not null comment '数量',
    unit_price          varchar(50)               not null comment '单价',
    tax_amount          varchar(50)               null comment '税费',
    actual_amount       varchar(50)               not null comment '实际金额',
    enabled             tinyint     default 1     not null comment '数据是否可用(1:可用,0:不可用(逻辑删除)) ',
    create_time         bigint                    not null comment '创建时间',
    update_time         bigint                    not null comment '修改时间',
    unit_exchange_score int         default 0     null comment '赠送积分(单个)',
    currency            varchar(20) default 'CNY' null comment '币种'
)
    comment '订单详情表';

create index order_detail_order_id_index
    on `order`.order_detail (order_id);

create table if not exists `order`.order_pay
(
    id                      bigint                    not null
        primary key,
    order_id                bigint                    not null comment '订单id',
    pay_type                int                       not null comment '支付方式(101:微信支付,102:支付宝支付,103:银联支付...)',
    pay_amount              varchar(50)               not null comment '支付金额',
    currency                varchar(20) default 'CNY' not null comment '币种',
    inner_pay_serial_number varchar(100)              not null comment '内部支付流水号',
    outer_pay_serial_number varchar(100)              null comment '外部支付流水号',
    status                  tinyint     default 0     null comment '支付状态(-2:超时取消,-1:用户取消,0:待支付,1:付款完成)',
    enabled                 tinyint     default 1     not null comment '数据是否可用(1:可用,0:不可用(逻辑删除))',
    create_time             bigint                    null comment '创建时间',
    update_time             bigint                    not null comment '修改时间',
    constraint order_pay_order_id_uindex
        unique (order_id)
)
    comment '订单支付表';

create table if not exists `order`.return_order
(
    id              bigint                    not null
        primary key,
    origin_order_id bigint                    null comment '原始正向单单号',
    return_order_id bigint                    not null comment '退货单单号',
    return_amount   varchar(50)               not null comment '退款金额',
    return_score    int         default 0     null comment '退回积分值(如果正向单中使用了积分抵扣,要退回)',
    return_type     tinyint                   not null comment '退款类型(1:仅退款,2:退货退款)',
    enabled         tinyint                   null comment '数据是否可用(1:可用,0:不可用(逻辑删除)  ',
    create_time     bigint                    not null comment '创建时间',
    update_time     bigint                    not null comment '修改时间',
    status          tinyint                   null comment '逆向单状态(-1:用户取消,0:待退款,1:退货完成,2:退款完成)',
    currency        varchar(20) default 'CNY' not null comment '币种'
)
    comment '退货单/逆向单';

create table if not exists `order`.return_order_article_code
(
    id                bigint                     not null
        primary key,
    return_order_id   bigint                     not null comment '逆向订单id',
    sku_code          varchar(50)                not null comment 'sku 编码',
    share_price       bigint                     not null comment '分摊金额',
    article_code      varchar(100)               not null comment '物品码',
    tax_amount        varchar(50) default '0.00' null comment '税费',
    article_code_type tinyint                    null comment '物品码类型(0:imei,1:sn)',
    enabled           tinyint     default 1      not null comment '数据是否可用(1:可用,0:不可用(逻辑删除))',
    create_time       bigint                     not null comment '创建时间',
    update_time       bigint                     null comment '更新时间',
    currency          varchar(20) default 'CNY'  null comment '币种'
)
    comment '订单物品码表';

create index return_order_article_code_return_order_id_index
    on `order`.return_order_article_code (return_order_id);

create table if not exists `order`.return_order_detail
(
    id              bigint                     not null
        primary key,
    return_order_id bigint                     not null comment '逆向订单id',
    sku_code        varchar(50)                not null comment 'sku 编码',
    quantity        int                        not null comment '数量',
    tax_amount      varchar(50) default '0.00' null comment '税费',
    return_amount   varchar(50)                not null comment '退款金额',
    enabled         tinyint     default 1      not null comment '数据是否可用(1:可用,0:不可用(逻辑删除)) ',
    create_time     bigint                     not null comment '创建时间',
    update_time     bigint                     not null comment '修改时间',
    currency        varchar(20) default 'CNY'  not null comment '币种'
)
    comment '逆向订单详情表';

create index return_order_detail_return_order_id_index
    on `order`.return_order_detail (return_order_id);

create table if not exists `order`.return_order_refund
(
    id                         bigint                    not null
        primary key,
    return_order_id            bigint                    not null comment '逆向订单id',
    return_refund_type         int                       not null comment '退款方式(101:微信支付,102:支付宝支付,103:银联支付...)',
    refund_amount              varchar(50)               not null comment '退款金额',
    currency                   varchar(20) default 'CNY' not null comment '币种',
    inner_refund_serial_number varchar(100)              not null comment '内部退款流水号',
    outer_refund_serial_number varchar(100)              null comment '外部退款流水号',
    status                     tinyint     default 0     null comment '支付状态(-1:用户取消,0:待退款,1:退款完成)',
    enabled                    tinyint     default 1     not null comment '数据是否可用(1:可用,0:不可用(逻辑删除))',
    create_time                bigint                    null comment '创建时间',
    update_time                bigint                    not null comment '修改时间',
    constraint order_pay_return_order_id_uindex
        unique (return_order_id)
)
    comment '逆向单退款表';







create table commodity_stock
(
	id bigint not null comment '库存主键',
	sku_code varchar(50) not null,
	enabled tinyint null comment '记录是否可用 0 不可用, 1 可用',
	stock_amount bigint null comment '库存数量',
	create_time long not null comment '创建时间',
	update_time Long not null comment '修改时间',
	constraint commodity_stock_pk
		primary key (id)
)
comment '商品库存表';

create unique index commodity_stock_sku_code_uindex
	on commodity_stock (sku_code);


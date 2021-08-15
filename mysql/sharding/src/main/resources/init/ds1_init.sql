create schema `order1` collate utf8_general_ci;

create table if not exists `order1`.`order1`
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
create table if not exists `order1`.`order2`
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
create table if not exists `order1`.`order3`
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
create table if not exists `order1`.`order4`
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
create table if not exists `order1`.`order5`
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
create table if not exists `order1`.`order6`
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
create table if not exists `order1`.`order7`
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
create table if not exists `order1`.`order8`
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
create table if not exists `order1`.`order9`
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
create table if not exists `order1`.`order10`
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
create table if not exists `order1`.`order11`
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
create table if not exists `order1`.`order12`
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
create table if not exists `order1`.`order13`
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
create table if not exists `order1`.`order14`
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
create table if not exists `order1`.`order15`
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
create table if not exists `order1`.`order16`
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
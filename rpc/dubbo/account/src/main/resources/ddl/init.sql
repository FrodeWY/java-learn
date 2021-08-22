create table account
(
    account_id      bigint                     not null comment '账户id'
        primary key,
    user_id         bigint                     not null comment '用户id',
    account_name    varchar(20)                not null comment '用户名',
    account_type    int                        not null comment '账户类型(人民币账户:1,美元账户:2....)',
    account_balance varchar(50) default '0.00' not null comment '账户金额',
    account_status  int         default 1      not null comment '账户状态(-1:注销,0:冻结,1:可用)',
    currency        varchar(20)                not null comment '货币',
    version         int         default 1      not null comment '版本号',
    modify_time     bigint                     null comment '修改时间',
    create_time     bigint                     not null comment '开户时间'
)
    comment '账户表';

create index account_user_id_account_type_account_status_index
    on account (user_id, account_type, account_status);



create table freeze_asset
(
    id             bigint auto_increment
        primary key,
    account_id     bigint                     not null comment '账户id',
    account_type   int                        not null comment '账户类型',
    amount         varchar(50) default '0.00' not null comment '锁定金额',
    transaction_no varchar(40)                not null comment '交易流水号',
    create_time    bigint                     not null comment '创建时间',
    modify_time    bigint                     not null comment '修改时间',
    freeze_status  tinyint     default 0      not null comment '冻结状态(-1:失效,0:冻结,1:解冻)',
    version        int         default 1      not null comment '版本',
    to_account_id  bigint                     not null comment '准备转入的账号id',
    to_amount      varchar(50)                null comment '准备转入的金额',
    user_id        bigint                     not null comment '用户id',
    constraint freeze_asset_transaction_no_uindex
        unique (transaction_no)
)
    comment '资产冻结表';




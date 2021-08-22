package com.account.service.impl;

import com.account.dao.AccountDao;
import com.account.dao.FreezeAssetDao;
import com.account.dao.bo.UpdateAccountAmountBO;
import com.account.dto.request.PersonalAccountExchangeRequestDTO;
import com.account.entity.Account;
import com.account.entity.FreezeAsset;
import com.account.enums.FreezeStatusEnum;
import com.account.service.AccountExchangeTransactionsService;
import com.account.util.DistributionIdGenerator;
import java.math.BigDecimal;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wangyang
 * @ClassName AccountExchangeTransactionsServiceImpl
 * @Description TODO
 * @Date 2021/8/21 下午10:12
 * @Version 1.0
 */
@DubboService(version = "1.0.0", group = "account")
@Service
public class AccountExchangeTransactionsServiceImpl implements AccountExchangeTransactionsService {

  private final AccountDao accountDao;
  private final FreezeAssetDao freezeAssetDao;


  public AccountExchangeTransactionsServiceImpl(AccountDao accountDao, FreezeAssetDao freezeAssetDao) {
    this.accountDao = accountDao;
    this.freezeAssetDao = freezeAssetDao;
  }

  @Override
  @Transactional
  public boolean personalAccountExchange(PersonalAccountExchangeRequestDTO requestDTO) {
    Long userId = requestDTO.getUserId();
    Integer fromAccountType = requestDTO.getFromAccountType();
    Integer toAccountType = requestDTO.getToAccountType();
    BigDecimal fromExchangeAmount = new BigDecimal(requestDTO.getFromAmount());
    Account toAccount = accountDao.queryByUserIdAndAccountType(userId, toAccountType);
    if (toAccount == null) {
      throw new RuntimeException("该用户不存在:" + toAccount + "转入账户类型");
    }
    Account fromAccount;
    while (true) {
      fromAccount = accountDao.queryByUserIdAndAccountType(userId, fromAccountType);
      if (fromAccount == null) {
        throw new RuntimeException("该用户不存在:" + fromAccountType + "转出账户类型");
      }
      BigDecimal fromAccountBalance = new BigDecimal(fromAccount.getAccountBalance());
      if (fromAccountBalance.compareTo(fromExchangeAmount) == -1) {
        throw new RuntimeException("该用户,转出账户类型:" + fromAccountType + "余额不足");
      }
      UpdateAccountAmountBO updateAccountAmountBO = getUpdateAccountAmountBO(fromAccountBalance.subtract(fromExchangeAmount), fromAccount);

      int i = accountDao.updateAccountAmount(updateAccountAmountBO);
      if (i == 1) {
        break;
      }
    }
    FreezeAsset freezeAsset = getFreezeAsset(requestDTO, fromAccount, toAccount.getAccountId());
    freezeAssetDao.insert(freezeAsset);

    return true;
  }

  private FreezeAsset getFreezeAsset(PersonalAccountExchangeRequestDTO requestDTO, Account fromAccount, Long toAccountId) {
    FreezeAsset freezeAsset = new FreezeAsset();
    freezeAsset.setId(DistributionIdGenerator.generateSingleId());
    freezeAsset.setUserId(requestDTO.getUserId());
    freezeAsset.setAccountId(fromAccount.getAccountId());
    freezeAsset.setToAccountId(toAccountId);
    freezeAsset.setAccountType(fromAccount.getAccountType());
    freezeAsset.setAmount(requestDTO.getFromAmount());
    freezeAsset.setTransactionNo(requestDTO.getTransactionNo());
    freezeAsset.setCreateTime(System.currentTimeMillis());
    freezeAsset.setModifyTime(System.currentTimeMillis());
    freezeAsset.setToAmount(requestDTO.getToAmount());
    freezeAsset.setFreezeStatus(FreezeStatusEnum.FREEZE.getValue());
    freezeAsset.setVersion(1);
    return freezeAsset;
  }

  private UpdateAccountAmountBO getUpdateAccountAmountBO(BigDecimal accountBalance, Account account) {
    UpdateAccountAmountBO updateAccountAmountBO = new UpdateAccountAmountBO();
    updateAccountAmountBO.setAccountId(account.getAccountId());
    updateAccountAmountBO.setVersion(account.getVersion());
    updateAccountAmountBO.setModifyTime(System.currentTimeMillis());
    updateAccountAmountBO.setAccountBalance(accountBalance.toString());
    return updateAccountAmountBO;
  }

  @Override
  public boolean personalAccountExchangeConfirm(PersonalAccountExchangeRequestDTO requestDTO) {
    FreezeAsset freezeAsset = freezeAssetDao.queryByTransactionNoAndFreezeStatus(requestDTO.getTransactionNo(), FreezeStatusEnum.FREEZE.getValue());
    if (freezeAsset == null) {
      return false;
    }
    BigDecimal toAmount = new BigDecimal(freezeAsset.getToAmount());
    while (true) {
      Account toAccount = accountDao.queryById(freezeAsset.getToAccountId());
      BigDecimal accountBalance = new BigDecimal(toAccount.getAccountBalance());
      UpdateAccountAmountBO updateAccountAmountBO = getUpdateAccountAmountBO(toAmount.add(accountBalance), toAccount);
      int i = accountDao.updateAccountAmount(updateAccountAmountBO);
      if (i == 1) {
        break;
      }
    }
    FreezeAsset update = new FreezeAsset();
    update.setId(freezeAsset.getId());
    update.setFreezeStatus(FreezeStatusEnum.INVALID.getValue());
    freezeAssetDao.update(update);
    return true;
  }

  @Override
  public boolean personalAccountExchangeConfirmCancel(PersonalAccountExchangeRequestDTO requestDTO) {
    FreezeAsset freezeAsset = freezeAssetDao.queryByTransactionNoAndFreezeStatus(requestDTO.getTransactionNo(), FreezeStatusEnum.FREEZE.getValue());
    if (freezeAsset == null) {
      return false;
    }
    BigDecimal amount = new BigDecimal(freezeAsset.getAmount());
    while (true) {
      Account account = accountDao.queryById(freezeAsset.getAccountId());
      BigDecimal accountBalance = new BigDecimal(account.getAccountBalance());
      UpdateAccountAmountBO updateAccountAmountBO = getUpdateAccountAmountBO(amount.add(accountBalance), account);
      int i = accountDao.updateAccountAmount(updateAccountAmountBO);
      if (i == 1) {
        break;
      }
    }
    FreezeAsset update = new FreezeAsset();
    update.setId(freezeAsset.getId());
    update.setFreezeStatus(FreezeStatusEnum.UNFREEZE.getValue());
    freezeAssetDao.update(update);
    return true;
  }
}

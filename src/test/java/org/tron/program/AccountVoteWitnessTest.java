package org.tron.program;

import com.google.common.collect.Lists;
import com.google.protobuf.ByteString;
import java.io.File;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tron.core.Constant;
import org.tron.core.capsule.AccountCapsule;
import org.tron.core.capsule.WitnessCapsule;
import org.tron.core.config.Configuration;
import org.tron.core.config.args.Args;
import org.tron.core.db.Manager;
import org.tron.protos.Protocol.AccountType;

public class AccountVoteWitnessTest {

  private static final Logger logger = LoggerFactory.getLogger("Test");
  private static Manager dbManager = new Manager();
  private static String dbPath = "output_witness";

  @BeforeClass
  public static void init() {
    //Args.setParam(new String[]{}, Configuration.getByPath(Constant.TEST_CONF));
    Args.setParam(new String[]{"-d", dbPath},
        Configuration.getByPath(Constant.TEST_CONF));

    dbManager.init();
  }

  @AfterClass
  public static void removeDb() {
    File dbFolder = new File(dbPath);
    deleteFolder(dbFolder);
  }

  private static void deleteFolder(File index) {
    if (!index.isDirectory() || index.listFiles().length <= 0) {
      index.delete();
      return;
    }
    for (File file : index.listFiles()) {
      if (null != file) {
        deleteFolder(file);
      }
    }
    index.delete();
  }

  @Test
  public void testAccountVoteWitness() {
    final List<AccountCapsule> accountCapsuleList = this.getAccountList();
    final List<WitnessCapsule> witnessCapsuleList = this.getWitnessList();
    accountCapsuleList.forEach(accountCapsule -> {
          dbManager.getAccountStore().put(accountCapsule.getAddress().toByteArray(), accountCapsule);
          this.printAccount(accountCapsule.getAddress());
        }
    );
    witnessCapsuleList.forEach(witnessCapsule ->
        dbManager.getWitnessStore().put(witnessCapsule.getAddress().toByteArray(), witnessCapsule)
    );
    dbManager.updateWitness();
    this.printWitness(ByteString.copyFrom("00000000001".getBytes()));
    this.printWitness(ByteString.copyFrom("00000000002".getBytes()));
    this.printWitness(ByteString.copyFrom("00000000003".getBytes()));
    this.printWitness(ByteString.copyFrom("00000000004".getBytes()));
    this.printWitness(ByteString.copyFrom("00000000005".getBytes()));
    this.printWitness(ByteString.copyFrom("00000000006".getBytes()));
    this.printWitness(ByteString.copyFrom("00000000007".getBytes()));

  }

  private void printAccount(final ByteString address) {
    final AccountCapsule accountCapsule = dbManager.getAccountStore().get(address.toByteArray());
    if (null == accountCapsule) {
      logger.info("address is {}  , account is null", address.toStringUtf8());
      return;
    }
    logger.info("address is {}  ,countVoteSize is {}", accountCapsule.getAddress().toStringUtf8(),
        accountCapsule.getVotesList().size());
  }

  private void printWitness(final ByteString address) {
    final WitnessCapsule witnessCapsule = dbManager.getWitnessStore().get(address.toByteArray());
    if (null == witnessCapsule) {
      logger.info("address is {}  , winess is null", address.toStringUtf8());
      return;
    }
    logger.info("address is {}  ,countVote is {}", witnessCapsule.getAddress().toStringUtf8(),
        witnessCapsule.getVoteCount());
  }

  private List<AccountCapsule> getAccountList() {
    final List<AccountCapsule> accountCapsuleList = Lists.newArrayList();
    final AccountCapsule accountTron = new AccountCapsule(
        ByteString.copyFrom("00000000001".getBytes()), ByteString.copyFromUtf8("Tron"),
        AccountType.Normal);
    final AccountCapsule accountMarcus = new AccountCapsule(
        ByteString.copyFrom("00000000002".getBytes()), ByteString.copyFromUtf8("Marcus"),
        AccountType.Normal);
    final AccountCapsule accountOlivier = new AccountCapsule(
        ByteString.copyFrom("00000000003".getBytes()), ByteString.copyFromUtf8("Olivier"),
        AccountType.Normal);
    final AccountCapsule accountSasaXie = new AccountCapsule(
        ByteString.copyFrom("00000000004".getBytes()), ByteString.copyFromUtf8("SasaXie"),
        AccountType.Normal);
    final AccountCapsule accountVivider = new AccountCapsule(
        ByteString.copyFrom("00000000005".getBytes()), ByteString.copyFromUtf8("Vivider"),
        AccountType.Normal);
    //accountTron addVotes
    accountTron.addVotes(accountMarcus.getAddress(), 100);
    accountTron.addVotes(accountOlivier.getAddress(), 100);
    accountTron.addVotes(accountSasaXie.getAddress(), 100);
    accountTron.addVotes(accountVivider.getAddress(), 100);

    //accountMarcus addVotes
    accountMarcus.addVotes(accountTron.getAddress(), 100);
    accountMarcus.addVotes(accountOlivier.getAddress(), 100);
    accountMarcus.addVotes(accountSasaXie.getAddress(), 100);
    accountMarcus.addVotes(ByteString.copyFrom("00000000006".getBytes()), 100);
    accountMarcus.addVotes(ByteString.copyFrom("00000000007".getBytes()), 100);
    //accountOlivier addVotes
    accountOlivier.addVotes(accountTron.getAddress(), 100);
    accountOlivier.addVotes(accountMarcus.getAddress(), 100);
    accountOlivier.addVotes(accountSasaXie.getAddress(), 100);
    accountOlivier.addVotes(accountVivider.getAddress(), 100);
    //accountSasaXie addVotes
    //accountVivider addVotes
    accountCapsuleList.add(accountTron);
    accountCapsuleList.add(accountMarcus);
    accountCapsuleList.add(accountOlivier);
    accountCapsuleList.add(accountSasaXie);
    accountCapsuleList.add(accountVivider);
    return accountCapsuleList;
  }

  private List<WitnessCapsule> getWitnessList() {
    final List<WitnessCapsule> witnessCapsuleList = Lists.newArrayList();
    final WitnessCapsule witnessTron = new WitnessCapsule(
        ByteString.copyFrom("00000000001".getBytes()), 0, "");
    final WitnessCapsule witnessOlivier = new WitnessCapsule(
        ByteString.copyFrom("00000000003".getBytes()), 100, "");
    final WitnessCapsule witnessVivider = new WitnessCapsule(
        ByteString.copyFrom("00000000005".getBytes()), 200, "");
    final WitnessCapsule witnessSenaLiu = new WitnessCapsule(
        ByteString.copyFrom("00000000006".getBytes()), 300, "");
    witnessCapsuleList.add(witnessTron);
    witnessCapsuleList.add(witnessOlivier);
    witnessCapsuleList.add(witnessVivider);
    witnessCapsuleList.add(witnessSenaLiu);
    return witnessCapsuleList;
  }
}
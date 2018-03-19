package cn.finder.hyperledger.fabric.example;


import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;

import org.apache.log4j.Logger;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.TransactionException;

import cn.finder.hyperledger.fabric.ChaincodeManager;
import cn.finder.hyperledger.fabric.FConfig;
import cn.finder.hyperledger.fabric.bean.Chaincode;
import cn.finder.hyperledger.fabric.bean.Orderers;
import cn.finder.hyperledger.fabric.bean.Peers;

/***
 * 某组织的客户端调用
 * @author finder
 *
 */
public class FabricManager {

	private static Logger logger =Logger.getLogger(FabricManager.class);
	
    private ChaincodeManager manager;

    private static FabricManager instance = null;

    public static FabricManager obtain()
            throws CryptoException, InvalidArgumentException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, TransactionException, IOException {
        if (null == instance) {
            synchronized (FabricManager.class) {
                if (null == instance) {
                    instance = new FabricManager();
                }
            }
        }
        return instance;
    }

    private FabricManager()
            throws CryptoException, InvalidArgumentException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, TransactionException, IOException {
        manager = new ChaincodeManager(getConfig());
    }

    /**
     * 获取节点服务器管理器
     * 
     * @return 节点服务器管理器
     */
    public ChaincodeManager getManager() {
        return manager;
    }

    /**
     * 根据节点作用类型获取节点服务器配置
     * 
     * @param type
     *            服务器作用类型（1、执行；2、查询）
     * @return 节点服务器配置
     */
    private FConfig getConfig() {
        FConfig config = new FConfig();
        config.setOrderers(getOrderers());
        config.setPeers(getPeers());
        config.setChaincode(getChaincode("xxx", "xxxcc", "github.com/hyperledger/fabric/chaincode/go/release/xxx", "1.0"));
        config.setChannelArtifactsPath(getChannleArtifactsPath());
        config.setCryptoConfigPath(getCryptoConfigPath());
        return config;
    }

    private Orderers getOrderers() {
        Orderers orderer = new Orderers();
        orderer.setOrdererDomainName("example.com");
        orderer.addOrderer("orderer1.example.com", "grpc://x.x.x.x:7050");
        orderer.addOrderer("orderer0.example.com", "grpc://x.x.x.xx:7050");
        orderer.addOrderer("orderer2.example.com", "grpc://x.x.x.xxx:7050");
        return orderer;
    }

    /**
     * 获取节点服务器集
     * 
     * @return 节点服务器集
     */
    private Peers getPeers() {
        Peers peers = new Peers();
        peers.setOrgName("XXX");
        peers.setOrgMSPID("XXXMSP");
        peers.setOrgDomainName("xxx.example.com");
        peers.addPeer("peer1.xxx.example.com", "peer1.xxx.example.com", "grpc://x.x.x.x:7051", "grpc://x.x.x.x:7053", "http://x.x.x.x:7054");
        return peers;
    }

    /**
     * 获取智能合约
     * 
     * @param channelName
     *            频道名称
     * @param chaincodeName
     *            智能合约名称
     * @param chaincodePath
     *            智能合约路径
     * @param chaincodeVersion
     *            智能合约版本
     * @return 智能合约
     */
    private Chaincode getChaincode(String channelName, String chaincodeName, String chaincodePath, String chaincodeVersion) {
        Chaincode chaincode = new Chaincode();
        chaincode.setChannelName(channelName);
        chaincode.setChaincodeName(chaincodeName);
        chaincode.setChaincodePath(chaincodePath);
        chaincode.setChaincodeVersion(chaincodeVersion);
        chaincode.setInvokeWatiTime(100000);
        chaincode.setDeployWatiTime(120000);
        return chaincode;
    }

    /**
     * 获取channel-artifacts配置路径
     * 
     * @return /WEB-INF/classes/fabric/channel-artifacts/
     */
    private String getChannleArtifactsPath() {
        String directorys = FabricManager.class.getClassLoader().getResource("fabric").getFile();
        logger.debug("directorys = " + directorys);
        File directory = new File(directorys);
        logger.debug("directory = " + directory.getPath());

        return directory.getPath() + "/channel-artifacts/";
    }

    /**
     * 获取crypto-config配置路径
     * 
     * @return /WEB-INF/classes/fabric/crypto-config/
     */
    private String getCryptoConfigPath() {
        String directorys = FabricManager.class.getClassLoader().getResource("fabric").getFile();
        logger.debug("directorys = " + directorys);
        File directory = new File(directorys);
        logger.debug("directory = " + directory.getPath());

        return directory.getPath() + "/crypto-config/";
    }

}
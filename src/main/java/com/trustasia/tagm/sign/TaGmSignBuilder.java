package com.trustasia.tagm.sign;

import com.trustasia.tagm.core.SignerException;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;

import javax.ws.rs.HttpMethod;
import java.io.IOException;
import java.net.URI;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringJoiner;

/**
 * 签名构造器
 *
 * @version 1.0
 * @Author robin.li
 * @Date 2025-04-09 17:03:13
 */
public class TaGmSignBuilder {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 签名随机数
     */
    private String random;

    /**
     * 时间戳
     */
    private String timestamp;

    /**
     * URI
     */
    private URI uri;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求body
     */
    private String body;

    /**
     * 客户端证书
     */
    private String clientCert;

    /**
     * 私钥
     */
    private String priKey;

    public static TaGmSignBuilder builder() {
        return new TaGmSignBuilder();
    }

    public TaGmSignBuilder random(String random) {
        this.random = random;
        return this;
    }

    public TaGmSignBuilder timestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public TaGmSignBuilder uri(URI uri) {
        this.uri = uri;
        return this;
    }

    public TaGmSignBuilder method(String method) {
        this.method = method;
        return this;
    }

    public TaGmSignBuilder body(String body) {
        this.body = body;
        return this;
    }

    public TaGmSignBuilder clientCert(String clientCert) {
        this.clientCert = clientCert;
        return this;
    }

    public TaGmSignBuilder priKey(String priKey) {
        this.priKey = priKey;
        return this;
    }

    public String build() {

        String requestUri = getSortedRequestUri(uri);
        StringJoiner stringJoiner = new StringJoiner("\n")
                .add(method)
                .add(timestamp + random)
                .add(requestUri);
        if (!method.equalsIgnoreCase(HttpMethod.GET)) {
            // 3、构建  body 体字符串
            String bodyToSign = sm3Hash(body);
            stringJoiner.add(bodyToSign);
        }

        X509Certificate x509Certificate = null;
        try {
            x509Certificate = PemUtil.getX509Certificate(clientCert);
        } catch (IOException | CertificateException e) {
            throw new SignerException("客户端证书解析失败", e);
        }
        PrivateKey privateKey = null;
        try {
            privateKey = PemUtil.convertToPrivateKey(priKey);
        } catch (IOException e) {
            throw new SignerException("私钥解析失败", e);
        }

        Signature signature = null;
        try {
            signature = Signature.getInstance(x509Certificate.getSigAlgName(),
                    BouncyCastleProvider.PROVIDER_NAME);
            signature.initSign(privateKey);
            signature.update(stringJoiner.toString().getBytes());
            byte[] sign = signature.sign();
            return Base64.toBase64String(sign);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | SignatureException | InvalidKeyException e) {
            throw new SignerException("签名构建异常", e);
        }
    }

    private String sm3Hash(String input) {
        byte[] data = input.getBytes();
        SM3Digest sm3 = new SM3Digest();
        sm3.update(data, 0, data.length);

        byte[] hash = new byte[sm3.getDigestSize()];
        sm3.doFinal(hash, 0);

        return Hex.toHexString(hash);
    }

    public static String getSortedRequestUri(URI uri) {
        String path = uri.getPath();
        String query = uri.getRawQuery();

        if (StringUtils.isBlank(query)) {
            return path;
        }

        String sortedQuery = sortQueryParameters(query);
        return path + "?" + sortedQuery;
    }

    private static String sortQueryParameters(String rawQuery) {
        String[] params = rawQuery.split("&");
        Arrays.sort(params, Comparator.comparing(param -> param.split("=")[0]));
        return String.join("&", params);
    }


}

package com.trustasia.tagm.sign;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @version 1.0
 * @Author robin.li
 * @Date 2025-04-09 18:41:22
 */
public class PemUtil {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static X509Certificate getX509Certificate(String cert) throws IOException, CertificateException {
        StringReader stringReader = new StringReader(cert);
        PemObject pemObject = readPemObject(stringReader);
        X509CertificateHolder x509CertificateHolder;
        if (pemObject == null) {
            // 如果不是 PEM 格式，则直接解码转换为 X509CertificateHolder
            x509CertificateHolder = new X509CertificateHolder(Base64.decode(cert));
        } else {
            x509CertificateHolder = new X509CertificateHolder(pemObject.getContent());
        }
        return new JcaX509CertificateConverter()
                .setProvider(BouncyCastleProvider.PROVIDER_NAME)
                .getCertificate(x509CertificateHolder);
    }

    public static PemObject readPemObject(Reader reader) throws IOException {
        try (PemReader pemReader = new PemReader(reader)) {
            return pemReader.readPemObject();
        }
    }

    public static PrivateKey convertToPrivateKey(String privateKeyStr) throws IOException {
        JcaPEMKeyConverter pemKeyConverter = new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME);
        PemReader pemReader = new PemReader(new StringReader(privateKeyStr));
        PemObject pemObject = pemReader.readPemObject();
        byte[] content = pemObject.getContent();
        return pemKeyConverter.getPrivateKey(PrivateKeyInfo.getInstance(content));
    }
}

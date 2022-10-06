/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.aws.greengrass.clientdevices.auth.iot;

import com.aws.greengrass.clientdevices.auth.configuration.RuntimeConfiguration;
import com.aws.greengrass.clientdevices.auth.iot.dto.CertificateV1;
import software.amazon.awssdk.utils.ImmutableMap;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;


public class CertificateRegistry {
    private final RuntimeConfiguration runtimeConfiguration;
    private final Map<Certificate.Status, CertificateV1.Status> domain2dtoStatus = ImmutableMap.of(
            Certificate.Status.ACTIVE, CertificateV1.Status.ACTIVE,
            Certificate.Status.UNKNOWN, CertificateV1.Status.UNKNOWN
    );
    private final Map<CertificateV1.Status, Certificate.Status> dto2domainStatus = ImmutableMap.of(
            CertificateV1.Status.ACTIVE, Certificate.Status.ACTIVE,
            CertificateV1.Status.UNKNOWN, Certificate.Status.UNKNOWN
    );

    @Inject
    public CertificateRegistry(RuntimeConfiguration runtimeConfiguration) {
        this.runtimeConfiguration = runtimeConfiguration;
    }

    /**
     * Retrieve certificate by certificate pem.
     *
     * @param certificatePem cert pem
     * @return certificate object
     * @throws InvalidCertificateException if certificate PEM is invalid
     */
    public Optional<Certificate> getCertificateFromPem(String certificatePem) throws InvalidCertificateException {
        Certificate cert = Certificate.fromPem(certificatePem);
        Optional<CertificateV1> certV1dto = runtimeConfiguration.getCertificateV1(cert.getCertificateId());
        return certV1dto.map(this::v1dto2Cert);
    }

    /**
     * Create and store a new certificate.
     * </p>
     * Certificates are created with an initial UNKNOWN state. Callers
     * are responsible for updating the appropriate metadata and then
     * calling {@link #updateCertificate(Certificate)}
     *
     * @param certificatePem Certificate PEM
     * @return certificate object
     * @throws InvalidCertificateException if certificate PEM is invalid
     */
    public Certificate createCertificate(String certificatePem) throws InvalidCertificateException {
        Certificate newCert = Certificate.fromPem(certificatePem);
        runtimeConfiguration.putCertificate(cert2v1dto(newCert));
        return newCert;
    }

    /**
     * Update certificate.
     *
     * @param certificate certificate object
     */
    public void updateCertificate(Certificate certificate) {
        runtimeConfiguration.putCertificate(cert2v1dto(certificate));
    }

    /**
     * Deletes a certificate from the repository.
     *
     * @param certificate certificate to remove
     */
    public void deleteCertificate(Certificate certificate) {
        runtimeConfiguration.removeCertificateV1(certificate.getCertificateId());
    }

    private Certificate v1dto2Cert(CertificateV1 dto) {
        Certificate cert = new Certificate(dto.getCertificateId());
        cert.setStatus(dto2domainStatus.get(dto.getStatus()), Instant.ofEpochMilli(dto.getStatusUpdated()));
        return cert;
    }

    private CertificateV1 cert2v1dto(Certificate cert) {
        return new CertificateV1(cert.getCertificateId(), domain2dtoStatus.get(cert.getStatus()),
                cert.getStatusLastUpdated().toEpochMilli());
    }
}

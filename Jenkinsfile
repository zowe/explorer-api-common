#!groovy

/**
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright IBM Corporation 2018, 2019
 */


node('ibm-jenkins-slave-nvm') {
  def lib = library("jenkins-library").org.zowe.jenkins_shared_library

  def pipeline = lib.pipelines.gradle.GradlePipeline.new(this)

  pipeline.admins.add("jackjia")

  pipeline.setup(
    github: [
      email                      : lib.Constants.DEFAULT_GITHUB_ROBOT_EMAIL,
      usernamePasswordCredential : lib.Constants.DEFAULT_GITHUB_ROBOT_CREDENTIAL,
    ],
    artifactory: [
      url                        : lib.Constants.DEFAULT_ARTIFACTORY_URL,
      usernamePasswordCredential : lib.Constants.DEFAULT_ARTIFACTORY_ROBOT_CREDENTIAL,
    ]
  )

  // we have a custom build command
  pipeline.build()

  pipeline.test(
    name          : 'Unit',
    operation     : {
        sh './gradlew test'
    },
    allowMissingJunit : true
  )

  pipeline.sonarScan(
    scannerServer   : lib.Constants.DEFAULT_SONARQUBE_SERVER
  )

  // define we need publish stage
  pipeline.publish(
    operation: {
      def version = env.PUBLISH_VERSION
      if (version) {
        echo "Publishing ${version} ..."
      } else {
        error 'Unable to determine publish version'
      }

      pipeline.gradle._updateVersion(version)
      withCredentials([
        usernamePassword(
          credentialsId: lib.Constants.DEFAULT_ARTIFACTORY_ROBOT_CREDENTIAL,
          usernameVariable: 'USERNAME',
          passwordVariable: 'PASSWORD'
        )
      ]) {
          sh "./gradlew publishArtifacts -Pdeploy.username=$USERNAME -Pdeploy.password=$PASSWORD"
      }
    }
  )

  // define we need release stage
  pipeline.release()

  pipeline.end()
}

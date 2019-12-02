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

  pipeline.admins.add("jackjia", "jcain", "stevenh")

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

  // This step is special distinguished from GradlePipeline. The purpose of this
  // step is set correct version pattern so Gradle publish stage works. This is
  // requird if we don't use GradlePipeline default publish method.
  pipeline.createStage(
    name: "Setup Version",
    isSkippable: false,
    stage: {
      def publishPath = pipeline.getPublishTargetPath()
      def customVersion = publishPath.split('/').last() + "-SNAPSHOT"
      if (!customVersion) {
        error 'Cannot determine release version'
      }
      pipeline.setVersion(customVersion)
      pipeline.gradle._updateVersion(customVersion)
      echo "Version defined in gradle.properties is:"
      sh 'cat gradle.properties | grep version='
    },
    timeout: [time: 2, unit: 'MINUTES']
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
    scannerTool     : lib.Constants.DEFAULT_LFJ_SONARCLOUD_SCANNER_TOOL,
    scannerServer   : lib.Constants.DEFAULT_LFJ_SONARCLOUD_SERVER,
    allowBranchScan : lib.Constants.DEFAULT_LFJ_SONARCLOUD_ALLOW_BRANCH,
    failBuild       : lib.Constants.DEFAULT_LFJ_SONARCLOUD_FAIL_BUILD
  )

  // define we need publish stage
  pipeline.publish(
    operation: {
      withCredentials([
        usernamePassword(
          credentialsId    : lib.Constants.DEFAULT_ARTIFACTORY_ROBOT_CREDENTIAL,
          usernameVariable : 'USERNAME',
          passwordVariable : 'PASSWORD'
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

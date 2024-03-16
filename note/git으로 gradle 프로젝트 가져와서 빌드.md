<nav>
    <a href=".." target="_blank">[Jenkins]</a>
</nav>

# git으로 gradle 프로젝트 가져와서 빌드

---

## 사전 준비?
- git
  ```shell
  $ docker exec -it jenkins-server bash
  jenkins@e1f342247c44:/$ git --version
  git version 2.39.2
  jenkins@e1f342247c44:/$ java --version
  openjdk 17.0.10 2024-01-16
  OpenJDK Runtime Environment Temurin-17.0.10+7 (build 17.0.10+7)
  OpenJDK 64-Bit Server VM Temurin-17.0.10+7 (build 17.0.10+7, mixed mode)
  ```
  - 이미 jenkins가 설치된 docker에는 git이 내장되어 있음.
  - 또 git, github과 관련된 플러그인들이 앞의 과정에서 설치되어 있음
  - 따라서 별도로 준비할 필요가 없음
- gradle
  - gradle 관련 플러그인들이 앞의 과정에서 설치 됨
  - 대부분의 gradle 기반 프로젝트들은 gradlew 파일을 실행하기만 하면 되므로 별도로 설치할 필요는 없음
- jdk
  - jenkins/jenkins:latest 기준 openjdk 17이 내장됨
  - 물론 jdk21 기반의 젠킨스를 사용하고 싶다면 jenkins/jenkins:jdk21 을 사용하면 됨

---

## 젠킨스 프로젝트 생성
- 소스: https://github.com/ttasjwi/jenkins
  - 간단한 스프링부트 프로젝트(gradle)

### Item 생성
![build-gradle-1](/imgs/build-gradle-1.png)

- name: my-gradle-project

### git 설정
![build-gradle-2](/imgs/build-gradle-2.png)

- 소스코드가 위치한 리포지토리 URL 기입
- credential: 자격증명이 필요할 경우 설정해야함
- Branch Specifier: 브랜치 지정

### 빌드 작업 설정
![build-gradle-3](/imgs/build-gradle-3.png)

```shell
chmod +x gradlew
./gradlew clean build
```
- gradlew 파일에 실행 권한을 부여
- clean build 실행
- apply, 저장

---

## 빌드 실행
![build-gradle-4](/imgs/build-gradle-4.png)

- 지금 빌드

```shell
+ chmod +x gradlew
+ ./gradlew clean build
Starting a Gradle Daemon (subsequent builds will be faster)
> Task :clean UP-TO-DATE
...
BUILD SUCCESSFUL in 12s
9 actionable tasks: 8 executed, 1 up-to-date
Finished: SUCCESS
```
```shell
$ cd /var/jenkins_home/workspace/my-gradle-project/

$ ls -al
total 68
drwxr-xr-x 9 jenkins jenkins 4096 Mar 15 14:14 .
drwxr-xr-x 4 jenkins jenkins 4096 Mar 15 14:14 ..
drwxr-xr-x 8 jenkins jenkins 4096 Mar 15 14:14 .git
-rw-r--r-- 1 jenkins jenkins  468 Mar 15 14:14 .gitignore
drwxr-xr-x 6 jenkins jenkins 4096 Mar 15 14:14 .gradle
-rw-r--r-- 1 jenkins jenkins  404 Mar 15 14:14 README.md
drwxr-xr-x 9 jenkins jenkins 4096 Mar 15 14:14 build
-rw-r--r-- 1 jenkins jenkins  880 Mar 15 14:14 build.gradle.kts
drwxr-xr-x 3 jenkins jenkins 4096 Mar 15 14:14 gradle
-rwxr-xr-x 1 jenkins jenkins 8692 Mar 15 14:14 gradlew
-rw-r--r-- 1 jenkins jenkins 2776 Mar 15 14:14 gradlew.bat
drwxr-xr-x 2 jenkins jenkins 4096 Mar 15 14:14 imgs
drwxr-xr-x 2 jenkins jenkins 4096 Mar 15 14:14 note
-rw-r--r-- 1 jenkins jenkins   29 Mar 15 14:14 settings.gradle.kts
drwxr-xr-x 4 jenkins jenkins 4096 Mar 15 14:14 src

$ cd build/libs
$ ls -al
total 24288
drwxr-xr-x 2 jenkins jenkins     4096 Mar 15 14:14 .
drwxr-xr-x 9 jenkins jenkins     4096 Mar 15 14:14 ..
-rw-r--r-- 1 jenkins jenkins     3582 Mar 15 14:14 jenkins-0.0.1-SNAPSHOT-plain.jar
-rw-r--r-- 1 jenkins jenkins 24855200 Mar 15 14:14 jenkins-0.0.1-SNAPSHOT.jar
```
- 빌드 결과물이 jar파일로 저장되어 있는 것을 확인할 수 있다.

---

## plain.jar 생성 막기
- `-plain.jar`는 실행 가능한 jar파일이 아니다. (애플리케이션 실행에 필요한 모든 의존성이 포함되지 않음)
- 애플리케이션 배포 관점에서는 저 jar 파일이 우리의 핵심 관심사이므로, plain.jar를 포함시키지 않고싶다.
- 이럴 경우, build.gradle 파일에서 다음을 수정해주면 된다.

### kotlin
```kotlin
tasks.named<Jar>("jar") {
	enabled = false
}
```

### groovy
```groovy
tasks.named("jar") {
	enabled = false
}
```

### 결과 확인
```shell
$ docker exec -it jenkins-server bash
$ cd /var/jenkins_home/workspace/my-gradle-project/
jenkins@e1f342247c44:~/workspace/my-gradle-project$ cd build/libs
jenkins@e1f342247c44:~/workspace/my-gradle-project/build/libs$ ls -al
total 24284
drwxr-xr-x 2 jenkins jenkins     4096 Mar 16 04:26 .
drwxr-xr-x 9 jenkins jenkins     4096 Mar 16 04:26 ..
-rw-r--r-- 1 jenkins jenkins 24855200 Mar 16 04:26 jenkins-0.0.1-SNAPSHOT.jar
```
- plain jar 파일이 생성되지 않고, 모든 의존성이 포함된 실행 가능한 jar 파일 하나만 생성된다.

---

<nav>
    <a href=".." target="_blank">[Jenkins]</a>
</nav>

# Hello World : Item 생성,수정,삭제

---

## Item 생성

![hello-world-1](/imgs/hello-world-1.png)

- Dashboard > item 생성
- item은 젠킨스의 작업 단위이다.
- 기본적인 상태에서, 플러그인을 따로 연동하지 않고 아이템을 생성할 예정이므로 Freestyle project를 설정하여 생성한다

![hello-world-2](/imgs/hello-world-2.png)

```shell
echo "Hello World!"
```

- 별다른 설정은 하지 않을 것
- Build Steps에서 빌드 과정에서 할 작업을 설정할 수 있는데, 여기서 Execute Shell 선택
- 콘솔에 문장을 출력하는 echo 명령어를 실행하도록 한다
- Apply, 저장

![hello-world-3](/imgs/hello-world-3.png)

대시보드에 돌아가보면 아이템 목록이 생성된 것을 확인할 수 있음


---

## Item 실행

![hello-world-4](/imgs/hello-world-4.png)

- Item 선택
- “지금 빌드”를 선택하여, 빌드를 실행하도록 함

![hello-world-5](/imgs/hello-world-5.png)

- 빌드가 잘 실행된걸 볼 수 있음
- Console Output 들어가서 확인

```shell
Started by user Administrator
Running as SYSTEM
Building in workspace /var/jenkins_home/workspace/Hello-World
[Hello-World] $ /bin/sh -xe /tmp/jenkins8301849424020251736.sh
+ echo Hello World!
Hello World!
Finished: SUCCESS
```
잘 실행된 것을 확인 가능

---

## Item 수정

![hello-world-6](/imgs/hello-world-6.png)

- item 상세페이지 들어가서 구성(Configuration) 들어가기
- item을 수정할 수 있음
- javac -version 을 추가하여 자바 버전을 출력할 수 있도록 해보자

```shell
Started by user Administrator
Running as SYSTEM
Building in workspace /var/jenkins_home/workspace/Hello-World
[Hello-World] $ /bin/sh -xe /tmp/jenkins13957355123519106437.sh
+ echo Hello World!
Hello World!
+ javac -version
javac 17.0.10
Finished: SUCCESS
```
- 다시 빌드해보면 자바 버전이 새로 출력되는 것을 확인할 수 있다.
- 빌드 결과물이 /var/jenkins_home/workspace/Hello-World 에 있다고 한다.

---

## 빌드 결과물 폴더

```shell
docker exec -it jenkins-server bash
```
- 아까 우리가 빌드 실행 시, 작업물이 /var/jenkins_home/workspace/My-First-Project 에 있다고 로깅이 찍혀나왔었다.
- 젠킨스 컨테이너의 터미널로 접속해서 확인해보자.

```shell
jenkins@e1f342247c44:/$ cd /var/jenkins_home/workspace

jenkins@e1f342247c44:~/workspace$ ls -al
total 12
drwxr-xr-x  3 jenkins jenkins 4096 Mar 14 15:07 .
drwxr-xr-x 14 jenkins jenkins 4096 Mar 14 15:12 ..
drwxr-xr-x  2 jenkins jenkins 4096 Mar 14 15:07 Hello-World
```
- 여기 실제 들어가서 확인해보면 우리의 item 폴더가 하나 있는 것을 확인해볼 수 있다.

```shell
jenkins@e1f342247c44:~/workspace$ cd Hello-World
jenkins@e1f342247c44:~/workspace/Hello-World$ ls -al
total 8
drwxr-xr-x 2 jenkins jenkins 4096 Mar 14 15:07 .
drwxr-xr-x 3 jenkins jenkins 4096 Mar 14 15:07 ..
```
- 안에 들어가보면 아무 것도 없다
- 우리가 실행한 item은 패키징을 아무 것도 하지 않기 때문에, 빌드 결과물이 없다

---

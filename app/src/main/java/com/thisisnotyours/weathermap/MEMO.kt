package com.thisisnotyours.weathermap

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

//edit:
// Coroutine 이란?
//코루틴은 스레드와 기능적으로 같지만, 스레드에 비교하면 좀 더 가변고 유연하며 한단계 더 진화된 병렬 프로그래밍을 위한 기술.
//edit- 하나의 스레드 내에서 여러개의 코루틴이 실행되는 개념.
//Dispatcher.Main = UI 변경등을 처리하는 '메인 스레드'.
//Dispatcher.IO = UI 변경등을 처리하고 입출력 연산을 처리하기에 적합한 'IO 스레드'.
//


//me:
// suspend = '중지하다' 라는 의미.
//coroutine 에서 suspend 의 정의 = a function that could be started, paused, and resume.
//                              = 시작하고 멈추고 다시 시작할 수 있는 함수.
// suspend 란 비동기 실행을 위한 중단 지점의 의미.
// 즉, 잠시 중단(suspend)한다는 의미이고, 잠시 중단한다면 언젠가는 다시 시작(resume)된다는 뜻.
// 비동기는 동시에 일어나지 않는다는 의미. 요청과 그 결과가 동시에 일어나지 않음. 요청한 그 곳에서 결과가 나타나지 않음.
// Asynchronous : 동시에 일어나지 않는. 비동기.
// 비동기 장점 : 결과가 주어지는데 시간이 걸리더라도 그 시간 동안 다른 작업이 가능해 자원의 효율적인 사용이 가능.
//       단점 : 설계가 동기보다 복잡함
// 동기식은 동시, 즉 요청과 그 결과가 동시에 일어난 다는 의미. (A노드와 B노드 사이의 트랜잭션을 동시에 맞추겠다는 것)
// 동기식의 장점: 설계가 간단하고 직권적.
//        단점: 결과를 볼 때까지 아무것도 못하고 대기해야함.

//※ suspend 가 없다면?
// ex) 하나의 thread 가 block 될 경우, 해당 thread 는 다른 작업을 할 수 없는 block 상태에 놓이게 됨.
//  즉, blocked 상태가 끝날 때 까지 해당 thread 는 중지 상태인 것. 아무것도 못하게 됨.
//  BUT, suspend function 을 사용하면, blocked 된 상태일 때,
//  그 작업을 suspend 하고 그 기간동안 thread 에서 다른작업을 수행할 수 있다. = 자원 효율적이다.

//me:
// suspend 함수 withContext.
//1) suspend fun
//   : 코루틴 함수를 정의하는 키워드.
//   : suspend 함수.
//   : 코루틴 실행 흐름을 기다릴 수 있는 함수.
// 기존의 비동기 작업은 콜백을 사용해 비동기 작업이 끝났을 때 시작할 작업을 넣어줬는데,
// 코루틴은 말 그대로 기다리고(suspend), 다시 그 지저부터 재개(resume)하는 것.
// 이 함수가 호출될 때, 기존 진행 코드는 멈춘다(suspend). 그리고 이 함수가 끝날 때 기존 진행 코드가 실행된다(resume).

//2) withContext + Dispatchers.IO
// 그럼 코루틴은 어떤 쓰레드에서 실행되는가?
// 바로 코루틴을 시작한 CoroutineContext 가 지정한 쓰레드에서 동작함.
// MainThread 에서 동작. 그리고 모든 suspend fun 은 자신을 호출한 코루틴의 Context 에서 동작하게 됨.
//me:
// withContext 는 CoroutineContext 를 실행인자와 그 suspend 함수를 인자로 받고 suspend 함수를 실행시킴.
// 그리고 부모 CoroutineContext 는 이 전체 실행이 끝나고 결과를 반환할 때까지 기다림.

//예를들어..

//suspend fun getMainWeatherData(lat: Double, lon: Double, appid: String?) {
//    withContext(Dispatchers.IO){
//        //....make network request
//    }
//}

//getMainWeatherData 이 함수 자체는 MainThread 코루틴에서 실행된다.
//하지만 그 MainThread 는 withContext 함수를 기다리기만 할 뿐임.
//실제 네트워크 통신은 withContext 의 인자로 주어진 Dispatcher.IO 에서 이루어짐.
//Dispatcher.IO 이 객체는 코루틴에서 기본 제공하는 CoroutineContext 중 하나임.
//Dispatcher: 영어사전적 의미: (열차/버스 등이 정시 출발하도록 관리하는) 운행관리원, 배치 담당자

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



//me:
// @Inject
//의존성 주입 (함수에 필요한 클래스 또는 참조변수나 객체에 의존하는 것)
//외부에서 객체를 생성해서 넣어주는 것을 '주입'이라함.
// me: 의존성 주입)
//-코드에서 모듈간의 연결을 의미.
//-일반적으로 둘 중 하나가 다른 하나를 어떤 용도를 위해 사용.
//-객체지향언어에서는 두 클래스간의 관계라고도 말함.
//-클래스간의 의존성이 불어들면 유지보수시 매우편함.





//me:
// 코루틴 Scope & launch
//CoroutineScope 는 사실 CoroutineContext 타입 필드를 launch 등의 확장 함수 내부에서 사용하기 위한 '매개체' 역할만 담당함.
//me: 코루틴은 항상 자신이 속한 스코프를 참조해야 한다. 이후에 cancel 로 모두 취소가 가능하다.
//ex)
//class LoginViewModel(
//    private val loginRepository: LoginRepository
//): ViewModel() {
//
//    fun login(username: String, token: String) {
//
//        // Create a new coroutine on the UI thread
//        viewModelScope.launch {
//            val jsonBody = "{ username: \"$username\", token: \"$token\"}"
//
//            // Make the network call and suspend execution until it finishes
//            val result = loginRepository.makeLoginRequest(jsonBody)
//
//            // Display result of the network request to the user
//            when (result) {
//                is Result.Success<LoginResponse> -> // Happy path
//                else -> // Show error in UI
//            }
//        }
//    }
//}
// 1) viewModelScope : viewModel KTX 확장 프로그램에 포함된 사전 정의된 CoroutineScope.  CoroutineScopes 는 하나 이상의 관련 코루틴을 관리함.
// 2) launch 는 코루틴을 만들고 함수 본문의 실행을 해당하는 디스패처에 전달하는 함수.
//    launch 가 새 코루틴을 만들며, I/O 작업용으로 완료되기 전에 login 함수가 계속 실행되어 결과를 반환함.











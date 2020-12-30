# SPRING_MVC

<details>
  <summary>서블릿의 생명주기</summary>
  <div markdown="1">
   
  ### **서블릿 (Servlet)**

1. 자바 엔터프라이즈 에디션은 웹 애플리케이션 개발용 스팩과 API을 제공한다. 그 중 가장 중요한 클래스중 하나가 HttpServlet이다.
2. 프로세스를 생성하는 것이 아닌 요청 당 프로세스 내의 쓰레드 (만들거나, 풀에서 가져다가) 사용한다.

서블릿 등장 이전에 사용하던 기술 CGI(Common Gateway Interface)는 요청 당 프로세스를 만들어 사용했다.

CGI와 비교하여 서블릿이 갖는 장점은 다음과 같다.

1. 요청 당 쓰레드를 만들어 사용하므로 **빠르다.**
2. Java 기반이기 때문에 **OS 플랫폼 독립적이다.**
3. Java가 지원하는 보안과 관련된 기능을 지원받아 **보안성이 좋다.**
4. 플랫폼 독립성과 비슷한 맥락에서 **이식성이 좋다.**

서블릿 엔진 또는 서블릿 컨테이너라고 불리는 서블릿 스팩을 구현하여 서블릿을 어떻게 초기화하고, 실행 그리고 사용하는지 라이플 사이클을 관리할 수 있는 톰캣, 제티, 언더토 등이 해주는 기능들이 많다.

1. 세션 관리
2. 네트워크 서비스
3. MIME 기반 메세지 인코딩/디코딩
4. 서블릿 생명주기 관리

서블릿 애플리케이션은 우리가 직접 실행할 수 없다. 서블릿 애플리케이션은 서블릿 컨테이너가 실행할 수 있기 때문에 반드시 서블릿 컨테이너를 사용해야 한다.

#### 서블릿의 생명주기

- 클라이언트가 HTTP 요청을 보내면 서블릿 컨테이너가 그 요청을 받아서 서블릿 인스턴스의 ``init()`` 메소드를 호출하여 초기화 한다.
  - 최초 요청을 받았을 때 한번만 초기화하고, 그 이후로는 이 과정을 생략한다.
- 서블릿이 초기화 된 다음부터 클라이언트의 요청을 처리할 수 있다.
  - 요청 하나당 쓰레드가 생성(또는 pool에서 꺼냄)되고 이때 서블릿의 ``service()``메소드가 실행된다.
    - ``service()``는 보통 HTTP 요청 방식에 따라 doGet또는 doPost등으로 처리를 위임한다.
- 서블릿 컨테이너 판단에 따라 서블릿을 메모리에서 내려야 할 시점에 ``destroy()``를 호출한다.



우리가 스프링 프로젝트를 진행할때 흔히 사용하는 @GetMapping("/hello") 는 실제로 아래와 같은 과정을 거치면서 실행되는 것이다.

```java
 public class HelloServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        System.out.println("init");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        System.out.println("doGet");
    }


    @Override
    public void destroy(){
        System.out.println("destroy");
    }
}

```



#### 서블릿 리스너란?

 웹 애플리케이션에서 발생하는 주요 이벤트를 감지하고 각 이벤트에 특별한 작업이 필요한 경우에 사용할 수 있다.

```java
public class MyListener implements ServletContextListener {
   	//서블릿 시작시 이벤트
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Context init");
        sce.getServletContext().setAttribute("name","KH");
    }
	
    //서블릿 종료 이후 이벤트
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("context destroy");
    }
}
```

​	**서블릿 리스너로 어떤걸 할 수 있을까?**

- 서블릿 컨테이너가 구동될 때 DB 커넥션을 맺어두고 커넥션을 서블릿 애플리케이션에서 만든 여러 서블릿에게 제공해줄 수 있다. 여러 서블릿에서는 서블릿 컨텍스트라는 곳에 들어있는 애트리뷰트에 접근할 수 있다.  (커넥션 풀)

- 서블릿 컨테이너가 종료되는 시점에는 서블릿 컨텍스트 리스너를 활용해서 DB 커넥션을 정리할 수도 있다.

  

#### 서블릿 필터란?

서블릿 컨테이너에 들어온 요청을 서블릿으로 보내기 전, 또는 서블릿이 작성한 응답을 클라이언트로 보내기 전에 특별한 처리가 필요한 경우 사용한다.

```java
public class MyFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        System.out.println("Filter Init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("Filter");
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {
        System.out.println("Filter Destroy");
    }
}
```

**리스너vs필터**

둘의 차이는 필터와 리스너의 개념을 떠올리면 뚜렷해진다.

필터는 클라이언트와 서블릿 사이에서 작업을 미리 수행하면서 요청이나 응답을 변경/수정하는 반면에, 리스너는 요청이나 응답에 관계 없이 특정 이벤트에 의해 실행된다.

 필터는 **Servlet Container**와 **Servlet**사이에서 작동하지만,

서블릿 리스너는 각각 **Servlet Container 밖, Servlet 밖에서 작동한다.**
  </div>
</details>


<details>
  <summary>Dispatcher Servlet이란?</summary>
  <div markdown="1">
    ### DispatcherServlet

요청 하나를 처리할 때마다 서블릿을 만들면 web.xml에 서블릿 설정이 계속해서 늘어난다. 여러 서블릿에서 공통적으로 처리하고 싶은 부분이 있는데 이러한 부분을 Filter로 해결 가능하지만, 디자인패턴으로 나온것이 DispatcherSEervlet 이다. 

모든 요청을 Controller 하나가 받아 해당 요청을 처리할 핸들러에게 요청을 분배하는 것이다.

스프링이 이러한 분배(Dispatch) 해주는 Front Controller 역할을 해주는 servlet을 이미 구현했는데, 이것이 바로 **DispatcherServlet** 이다.

- 스프링 MVC의 핵심
- Front Controller 역할을 한다.

![DispatcherServlet1](C:\Users\jungk\Desktop\마크다운 정리\PICTURE\DispatcherServlet1.PNG)



#### DIspatcherServlet 동작 원리

1. 요청을 분석한다.
2. 핸들러 맵핑에게 위임하여 요청을 처리할 핸들러를 찾는다.
3. (등록되어 있는 핸들러 어댑터 중) 해당하는 핸들러를 실행할 수 있는 핸들러 어댑터를 찾는다.
4. 찾아낸 핸들러 어댑터를 사용해서 핸들러의 응답을 처리한다.
   - 핸들러의 리턴값을 보고 어떻게 처리할지 판단한다.
     - 대표적으로 @Controller 또는 @RestController
     - 뷰orJSON값
     - @ResonseEntity가 있다면 Converter(데이터 바인딩)를 사용해서 응답 본문을 만든다.
5. 만약 예외가 발생했다면 예외 처리 핸들러에 요청 처리를 위임한다.
6. 최종적으로 응답을 보낸다.

**HandlerMapping : 핸들러를 찾아주는 인터페이스**

**HandlerAdapter : 핸들러를 실행하는 인터페이스**

**viewResolver** : viewResolver를 직접 Bean으로 등록하지 않으면 기본 전략으로 **InternalResourceViewResolver**를 사용한다.

**InternalResourceViewResolver**는 직접 커스텀 가능하다.

```java
@Configuration // Bean 설정 파일(XML 파일을 대체)임을 알려준다.
@ComponentScan // Bean으로 등록되기 위한 Annotation이 부여된 클래스들을 자동으로 IoC Container에 등록되도록 한다.
public class WebConfig {
    
    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB_INF/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }
}
```

ViewResolver를 init하는 initViewResolvers() method도 Servlet 생명주기에 의해서 최초 요청시에만 호출되고 이 후에는 init을 다시 호출하지 않는다.





### DispatcherServlet의 인터페이스

DispatcherServlet.properties에 기본 전략이 설정되어 있다.

특정 타입에 해당하는 빈을 찾고 없으면 기본 전략을 사용한다.

- MultipartResolver
  - 파일 업로드 요청 처리에 필요한 인터페이스
  - HttpServeltReqeust를 MultiPartServletRequest로 변환해주어 요청이 담고있는 File을 꺼낼 수 있는 API를 제공
  - 기본 DIspatcherServlet에는 아무런 MultipartResolver Bean을 등록하지 않는다.  **스프링 부트** 사용시 기본적으로 하나가 등록되어 있어  아무런 설정 없이 File 업로드를 처리하는 Handler를 쉽게 만들 수 있다. (StandardServletMulipartResolver가 등록된다.)
- LocaleResolver
  - 클라이언트위 위치(Locale/어느나라 인지) 정보를 파악하는 인터페이스
  - 기본 전략은 요청의 aceept-language를 보고 판단한다.
- HandlerMapping 
  - 어떤 요청이 들어왔을때, 그 요청을 처리하는 **핸들러를 찾아주는** 인터페이스
  - @GetMapping과 같은 에노테이션 기반의 **RequestMappingHandlerMapping**
  - 빈 이름 기반의 **BeanNameHandlerMapping**
- HandlerAdapter
  - ``HandlerMapping``이 찾아낸 핸들러를 실행하는 인터페이스
  - 스프링 MVC 확장력의 핵심
  - HttpRequestHandlerAdapter, RequestMappingHandlerAdapter, SimpleControllerHandlerAdapter가 기본적으로 등록되어 있다.
- RequestToViewNameTranslator
  - 핸들러에서 뷰 이름을 명시적으로 리턴하지 않은 경우, 요청을 기반으로 뷰 이름을 판단하는 인터페이스
  - 예를들어 GetMapping("/events") 로 요청했을경우 events라는 이름의 뷰를 리턴한다.
- HandlerExceptionResolvers
  - 요청 처리 중에 발생한 에러를 천리하는 인터페이스
- ViewResolver
  - 뷰 이름(String)에 해당하는 뷰를 찾아내는 인터페이스
  - 뷰 이름을 따로 지정하지 않으면 URL을 기반으로 뷰 이름을 찾아 리턴함.
- FlashMapManager
  - FlashMap 인스턴스를 가져오고 저장하는 인터페이스
  - FlashMap은 주로 리다이렉션을 사용할 때 요청 매개변수를 사용하지 않고도 데이터를 정리할 때 사용한다.
  - redirect:/events



#### 스프링 부트를 사용하지 않는 스프링 MVC

- 서블릿 컨테이너(톰캣)에 등록한 웹 어플리케이션(WAR)에 DispatcherServlet을 등록한다.

  - web.xml에 서블릿 등록
  - 또는 WebApplicationInitializer에 자바 코드로 서블릿 등록

  ```java
  public class WebApplication implements WebApplicationInitializer {
          
      @Override
      public void onStartup(ServletContext servletContext) throws ServletException {
          AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
          context.register(WebConfig.class);
          context.refresh();
              
          DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
          ServletRegistration.Dynamic app = servletContext.addServlet("app", dispatcherServlet);
          add.addMapping("/app/*");
      }
  }
  ```

  - 세부 구성 요소는 사용자가 빈 설정을 어떻게 하느냐에 따라서 다르다.

- **@EnableWebMvc**

  Annotation 기반 Spring MVC를 손쉽게 쓸 수 있게 해주는 Annotation이다.

  ```java
  @Configuration
  @EnableWebMvc
  public class WebCofig{
      
  }
  ```

  @EnableWebMvc는 DelegatingWebMvcConfiguration라는 것을 읽어오는데(import) 어딘가에 위임을 해서 읽어오는 Deligation구조로 되어 확장성을 높이도록 되어있다. 따라서 손쉽게 기존에 있는 Bean( ex) HandlerMapping, HandlerAdapter)에 Interceptor나 MessageConverter 등을 추가할 수 있다.

  처음부터 HandlerMapping, HandlerAdapter 등을 등록하는 것이 아니라 WebMvcConfigurationSupport 클래스가 제공하는 HandlerMapping, HandlerAdapter를 조금만 수정하는식으로 Bean 설정을 할 수 있다.

  

- **WebMvcConfigurer** 를 implements

  @EnableWebMvc와 맞물려 돌아가며 스프링 MVC가 지원하는 인터페이스로 스프링 MVC를 커스터마이징할 때 주로 사용하는 인터페이스이다.

  예를 들어 @EnableWebMvc가 등록해주는 ViewResolver를 커스터마이징할 수 있다.

  ```java
  @Configuration
  @ComponentScan
  @EnableWebMvc
  public class WebConfig implements WebMvcConfigurer {
      
      @Override
      public void configureViewResolvers(ViewResolverRegistry registry) {
          registry.jsp("/WEB_INF/", ".jsp");
      }
  }
  ```

  


#### 스프링 부트를 사용하는 스프링 MVC

- 자바 애플리케이션에 내장 톰캣을 만들고 그 안에 DispatcherServlet을 등록한다.

  - 스프링 부트가 자동으로 설정해주고, 스프링 부트의 주관에 따라 여러 인터페이스 구현체를 빈으로 등록한다.

- 스프링 부트에서의 HandlerMapping

  1. SimpleUrlHandlerMapping
     - 파비콘 처리를 위한 핸들러
  2. RequestMappingHandlerMapping
     - Annotation 기반의 MVC 처리를 위한 핸들러
  3. BeanNameUrlHandlerMapping
     - 지금은 딱히 쓰지 않지만 사용 가능하다.
  4. SimpleUrlHandlerMapping
     - 스프링 부트가 지원하는 정적 리소스 지원 기능이 이 핸들러 때문에 가능하다.
     - 디렉토리 안에 들어있는 정적 리소스에 리소스 핸들러 매핑을 적용하면 캐시관련 정보가 응답헤더에 추가된다. 따라서 리소스를 효율적으로 사용할 수 있다. 예를 들어 리소스가 변경되지 않았으면 304(Not Modified)라는 응답을 보내 브라우저가 캐싱하고 있는 리소스를 그대로 사용하도록 한다.

- 스프링 부트에서의 HandlerAdapter

  1. RequestMappingHandlerAdapter
     - Annotation 기반의 MVC 처리를 위한 핸들러를 처리
  2. HttpReqeustHandlerAdapter
  3. SimpleControllerHandlerAdapter

- 스프링 부트에서의 ViewResolvers

  ContentNegotiatingViewResolver

  - 자기가 직접 View 이름에 해당하는 View를 찾는 것이 아니라 일을 다른 ViewResolver에게 위임한다.
  - 요청을 분석해서 아래 4개의 ViewResolver 중에서 가장 적절한 ViewResolver를 선택한다.

  1. BeanNameViewResolver
  2. ThymeleafViewResolver (의존성에 타임리프 추가)
  3. ViewResolverComposite
  4. InternalResourceViewResolver

  

  실질적으로 DispatcherServlet이 사용하고 있는 HandlerMapping, HandlerAdapter, ViewResolvers 등은 (External Libraries 안의) spring-boot-autoconfigure이라는 jar파일을 열어보면 META-INF spring.factories파일이 있다. 이 안에 EnableAutoConfiguration에 해당하는 모든 자동 설정 파일이 조건에 따라 적용된다. 이 설정 중 DispatcherServlet 설정(DispatcherServletAutoConfiguration)을 찾아 볼 수 있다. WebMvcAutoConfiguration 설정은 Spring MVC 자동 설정이다.

### 스프링 MVC 커스터마이징하는 방법(종류) 정리

1. application.properties(yml)
   - 스프링 부트의 자동 설정 안에서 properties들이 사용되는데, properties들은 @ConfigurationProperties를 사용하여 전부 prefix가 spring.resources로 설정되어 있다. 따라서 값들을 application.properties에서 설정하고 읽어올 수 있다.
   - 가장 손을 덜 대고 커스터마이징 하는 방법이다.
2. @Configuration + WebMvcConfigurer implements
   - 스프링 부트의 스프링MVC 자동 설정을 Default로 깔아놓고 추가 설정 하는 방법이다.
3. @Configuration + @EnableWebMvc + WebMvcConfigurer impelments 
   - 스프링 부트의 스프링 MVC 자동설정을 사용하지 않는 방법. (비추천/설정할게 너무 많음. 설정하다가 포기하는 경우 발생)
  </div>
</details>


<details>
  <summary>스프링 MVC 설정</summary>
  <div markdown="1">
   ### Formatter 사용하는 방법 알아보기

```java
@RestController
public class SampleController {
    @GetMapping("/hello/{name}")
    public String hello(@PathVariable("name") Person person){
        return "hello " + name;
    }
}
```

우리가 view에서 name값을 받으려고 할 때, name은 String이기때문에 Person형 변수인 person은 그 값을 받지 못하게 된다.

따라서 문자열 값을 프로퍼티 타입에 맞춰 변환하여 할당해주는 데이터 바인딩 작업이 필요한데 Formatter가 그 중 하나이다.

```java
public class PersonFormatter implements Formatter<Person> {
    @Override
    public Person parse(String s, Locale locale) throws ParseException {
        Person person = new Person();
        person.setName(s);
        return person;
    }

    @Override
    public String print(Person person, Locale locale) {
        return null;
    }
}
```

String -> 프로퍼티 타입으로 변환하기 위해 Formatter 인터페이스를 구현한 구현체를 만들고 ``parse``메소드를 재정의했다.

이렇게 Fomatter 클래스를 작성한 후, 앞에서 배운 ``WebMvcConfigurer``을 구현해서 적용시켜야 한다.

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new PersonFormatter());
    }
}

```

하지만 스프링부트를 사용한다면 이러한 코드가 필요가 없다.

``WebConfig`` 클래스를 삭제하고, 아주 간단하게 ``PersonFormatter``클래스에 ``@Component``만 붙혀주면 알아서 빈으로 등록된다.



**하지만, 실무에서는 대부분 중북의 위험이 있는 name과 같은 값으로 찾지 않는다. ID와 같은 유니크 제약조건이 걸린 값을 이용한다. 따라서 우리가 Spring Data JPA**를 사용한다면 이렇게 도메인(Entity) 관련한 Converter 또는 Formatter를 직접 만드는 경우는 극히 드물것이다.

Spring Data JPA가 자동으로 처리해주기 때문이다.



### HandlerInterceptor

1. HandlerMapping (어떤 요청이 들어왔을때, 어떤 핸들러를 사용할지 찾아주는 인터페이스)에 설정할 수 있는 인터셉터

2. 핸들러를 실행하기 전(pre) , 후(post)그리고 완료(after) 시점에 부가적인 작업을 하고싶은 경우 사용

3. 여러 핸들러에서 반복적으로 사용하는 코드를 줄이고 싶을 때 사용할 수 있다.



- boolean preHandle(request, response, handle) 
  - 핸들러 실행하기 전 호출
  - "핸들러"에 대한 정보를 사용할 수 있기 때문에 서블릿 필터에 비해 보다 세밀한 로직을 구현할 수 있다(스프링 빈에 접근이 가능하다. 물론 필터도 접근 가능)
  - 리턴값으로 계속 다음 인터셉터 또는 핸들러로 요청,응답을 전달할지(true 일 경우), 또는 응답 처리가 이곳에서 끝났는지(false) 알린다.
- void postHandle(requset, response, modelAndView)
  - 핸들러 실행이 끝나고 아직 뷰를 랜더링 하기 이전에 호출 됨
  - "뷰"에 전달할 추가적이거나 여러 핸들러에 공통적인 모델 정보를 담는데 사용할 수도 있다.
  - 이 메소드는 인터셉터 역순으로 호출된다.
  - 비동기적인 요청 처리 시에는 호출되지 않는다.
- void afterCompletion(request, response, handler, ex)
  - 요청 처리가 완전히 끝난 뒤(뷰 랜더링이 끝난 뒤)에 호출된다.
  - preHandler에서 true를 리턴한 경우에만 호출된다.
  - 이 메소드는 인터셉터 역순으로 호출된다.
  - 비동기적인 요청 처리시에는 호출되지 않는다(Restcontroller)

### 리소스 핸들러

이미지, 자바스크립트, CSS 그리고 HTML 파일과 같은 정적인 리소스를 처리하는 핸들러를 등록할때 사용

```java
@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/mobile/**")
                .addResourceLocations("classpath:/mobile/")
                .setCacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES));
    }
```

- 위 코드처럼 등록하면 ``resource/mobile`` 경로에 있는 정적 파일들을 등록할 수 있다.
- **스프링 부트** 사용시 기본적으로 설정이 되어있기 때문에 변경할 일이 많지는 않다.



#### HTTP 메시지 컨버터

1. ``@RequestParam``을 통해 요청 받은 body의 값을  String형태로 응답할 수 있다.

   ```java
   @GetMapping("/message")
       public String message(@RequestBody String body) {
           return body;
       }
   }
   
    @Test
       @DisplayName("문자열 컨버터 JSON -> String")
       public void messageConverter() throws Exception {
           this.mockMvc.perform(get("/message")
                   .content("hello"))
                   .andDo(print())
                   .andExpect(status().isOk())
                   .andExpect(content().string("hello"));
       }
   ```

2. JSON으로 받은 요청의 본문(@RequestBody, json 값을)를 Person 형으로 받거나, @ResponseBody를 통해 person형으로 응답에 실어 보낼 수 있다.

   ```java
    @GetMapping("/jsonMessage")
       public Person jsonMessage(@RequestBody Person person) {
           return person;
       }
   }
       @Test
       public void jsonMessage() throws Exception {
           Person person = new Person();
           person.setId(2019L);
           person.setName("kh");
           String jsonString = objectMapper.writeValueAsString(person);
   
           this.mockMvc.perform(get("/jsonMessage")
           .contentType(MediaType.APPLICATION_JSON)
           .accept(MediaType.APPLICATION_JSON)
           .content(jsonString))
                   .andDo(print())
                   .andExpect(status().isOk());
       }
   ```



### 기타 WebMvcConfigurer 설정

- ArgumentResolver 설정(addArgumentResolvers)

  스프링 MVC가 제공하는 기본 Argument Resolver 이외에 커스텀을 추가하고 싶을때

- Vew Controller 설정(addViewControllers)

  특정 URL 요청을 특정 뷰로 연결하고 싶을 때 사용한다.

  ```java
  @Controller
  public class SampleController {
    
    @GetMapping("/hi")
    public String hi() { return "hi"; }
  }
  ```

  이러한 코드를 작성하지 않고, 아래의 코드로 대체 가능

  ```java
  @Configuration
  public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
      registry.addViewController("/hi").setViewName("hi");
    }
  }
  ```

  </div>
</details>

<details>
  <summary>Dispatcher Servlet 활용 : HTTP Reqeust/Response</summary>
  <div markdown="1">
   ### Dispatcher Servlet 활용1 : HTTP Request/Response

1. HTTP 요청 여러가지 방법들

   ```java
   @GetMapping("/hello")
       @ResponseBody
       public String helloKH() {
           return "hello";
       }
   
       @GetMapping({"/hello", "/hello2"})
       @ResponseBody
       public String helloKH2() {
           return "hello";
       }
   
   
       @GetMapping("/hello/**") // /hello/All
       @ResponseBody
       public String helloKH3() {
           return "hello";
       }
   ```

2. 특정 타입의 데이터를 담고 있는 요청만 처리하기 (**consumes를 사용하면 된다.**)

   ```java
   @GetMapping(value = "/hello", consumes = MediaType.APPLICATION_JSON_VALUE)
       @ResponseBody
       public String hello() {
           return "hello";
       }
   ```

   만약,  클래스(핸들러)단에서 consumes를 먼저 설정하고 나서 메소드에서 다시한번 설정하면

   클래스에있던 설정은 무시된다.

3. 특정 파라미터가 있는 경우에만 요청을 처리하기

   ```java
    @GetMapping(value = "/hello", params = "name=kh")
       @ResponseBody
       public String hello2() {
           return "hello";
       }
   ```

4. HTTP method의 HEAD & OPTIONS

   - HEAD : GET요청과 동일하지만 응답 본문을 받아오지 않고 응답 헤더만 받아온다.

     - 요청 데이터가 있으면 안된다.
     - 리소스 정보만 간략하게 확인할때 사용된다.
     - BODY는 응답에서 제외된다.

   - OPTIONS : 서버 또는 특정 리소스가 제공하는 기능을 확인하는데 사용한다.

     - 사용할 수 있는 HTTP method 를 제공해준다.

     - 서버는 Allow 응답 헤더에 사용할 수 있는 HTTP method를 제공해야 한다.

       

     ```java
     	@GetMapping("/hello3")
         public String hello() {
             return "hello";
         }
     
     	@PostMapping("/hello3")
         public String helloPost() {
             return "hello";
         }
     ```

     이렇게 GET과 POST 모두 가능한 URI가 있다고 가정해보자.

     ```
     @DisplayName("HTTP OPTIONS 요청 사용하기")
     @Test
     public void helloPostTest() throws Exception {
         mockMvc.perform(options("/hello3"))
                 .andDo(print())
                 .andExpect(status().isOk());
     }
     ```

     테스트를 돌리면 정상적으로 실행되고, 실제로 사용했을때 보여지는 값은 [GET,POST,HEAD,OPTIONS] 이다.

5. 특정 패턴으로 요청 매핑하기

   - ?

     - 한글자
     - ex) "/author/???" 의 경우 "/author/123" 과 매핑된다.

   - *

     - 여러글자
     - ex) "/author/*" 의 경우 "/author/kh" 와 매핑된다.

   - **

     - 여러 패스
     - ex) "/author/**" 의 경우 "/author/kh/book" 과 매핑된다.

   - 패턴이 중복되는 경우

     ```java
     @Controller
     @RequestMapping("/hello")
     public class SampleController {
       
       @RequestMapping("/keesun")
       @ResponseBody 
       public String helloKeesun() {
         return "hello" + name;
       }
       
       @RequestMapping("/**")
       @ResponseBody 
       public String hello() {
         return "hello" + name;
       }
     }
     ```

     - 패턴이 중복되면 가장 구체적인 형태 (여기서는 /keesun) 로 매핑된다.

6. 특정 헤더가 있거나 없는 요청을 처리하고 싶은 경우

   - **HttpHeaders.FROM** 이라는 헤더가 존재하는 경우에만 매핑 받기

   ```java
   @Controller
   public class SampleController {
     
     @GetMapping(value = "/hello", headers = HttpHeaders.FROM)
     @ResponseBody 
     public String hello() {
       return "hello";
     }
   }
   
   //-----Test-------
   @Test
     public void helloTest() throws Exception {
       mockMvc.perform(get("/hello")
                   .header(HttpHeaders.FROM, "localhost"))
         			.andDo(print())
         			.andExpect(status().isOk())
         			.andExpect(content().string("hello"));
     }
   }
   ```

   - **HttpHeaders.FROM** 이라는 헤더가 없는 경우에만 매핑 받기

   ```java
   @Controller
   public class SampleController {
     
     @GetMapping(value = "/hello", headers = "!" + HttpHeaders.FROM)
     @ResponseBody 
     public String hello() {
       return "hello";
     }
   }
   
   //------Test-------
     @Test
     public void helloTest() throws Exception {
       mockMvc.perform(get("/hello")
                   .header(HttpHeaders.AUTHORIZATION, "111"))
         			.andDo(print())
         			.andExpect(status().isOk())
         			.andExpect(content().string("hello"));
     }
   }
   ```

   
  </div>
</details>

<details>
  <summary>Dispatcher servlet 활용 : 핸들러 메소드</summary>
  <div markdown="1">
   
   ### Dispatcher servlet 활용2 : 핸들러 메소드

1. ##### @PathVariable

   - 요청 URL 패턴의 일부를 핸들러 메소드의 아규먼트로 받는 방법
   - 타입 변환을 자동으로 지원해준다
     - EX) String으로 들어온 값을 자동으로 Integer형으로 변환해준다.
   - Optional 지원
     - @PathVariable(require = false)와 같다.

   ```java
   @Controller
   public class SampleController {
     
     @GetMapping("/events/{id}")
     @ResponseBody
     public Event getEvent(@PathVariable Integer id) {
       Event event = new Event();
       event.setId(id);
       
       return event;
     }
   }
   //-----Test-----
    @Test
     public void deleteEvents() throws Exception {
       mockMvc.perform(get("/events/1"))
         			.andDo(print())
         			.andExpect(status().isOk())
         			.andExpect(jsonPath("id").value(1));
     }
   }
   ```

2. ##### @MatrixVariable

   - 요청 URI 패턴에서 키/값 쌍의 데이터를 메소드 아규먼트로 받는 방법

   - 타입 변환 지원

   - 기본값이 반드시 있어야 한다.

   - Optional 지원

   - 이 기능은 기본적으로 비활성화 되어있다. 따라서 아래와 같은 설정이 필요하다

     ```java
     @Configuration
     public class WebConfig implements WebMvcConfigurer {
       
       @Override
       public void configurePathMatch(PathMatchConfigurer configurer) {
         UrlPathHelper urlPathHelper = new UrlPathHelper();
         urlPathHelper.setRemoveSemicolonContent(false);
         configurer.setUrlPathHelper(urlPathHelper);
       }
     }
     ```

     ```java
     @GetMapping("/event/{id}")
         @ResponseBody
         public Event getEvent2(@PathVariable Integer id, @MatrixVariable String name) {
             Event event = new Event();
             event.setId(id);
             event.setName(name);
             return event;
         }
     //-------Test-------
     
       
       @Test
       public void deleteEvents() throws Exception {
         mockMvc.perform(get("/events/1;name=keesun"))
           			.andDo(print())
           			.andExpect(status().isOk())
           			.andExpect(jsonPath("id").value(1));
       }
     }
     ```

3. ##### 요청 매개변수 (단순 타입) : @RequestParam

   - 요청 매게변수에 들어있는 단순 타입 데이터를 메소드 아규먼트로 받을 수 있다.
   - 값이 반드시 있어야 한다.
     - required=false 또는 Optional을 사용해서 부가적인 설정을 할수는 있다.
   - String이 아닌 값들은 자동으로 컨버전을 지원한다.
   - Map<String,String>또는 MultiValueMap<String, String>에 사용해서 모든 요청 매개변수를 받아 올 수 있다.
   - 이 Annotation은 생략이 가능하다.

   **요청 매개변수**

   - 쿼리 매개변수
   - 폼 데이터

   ```java
   @GetMapping("/events")
     @ResponseBody
     public Event getEvent(@RequestParam String name) {
       Event event = new Event();
       event.setName(name);
       return event;
     }
   //------Test-----
     @Test
     public void deleteEvents() throws Exception {
       mockMvc.perform(get("/events")
               	 .param("name", keesun))
         			.andDo(print())
         			.andExpect(status().isOk())
         			.andExpect(jsonPath("name").value("keesun"));
     }
   }
   ```

4. ##### @ModelAttribute

   - 여러 곳에 있는 단순 타입 데이터를 복합 타입 **객체**로 받아오거나 해당 객체를 새로 만들 때 사용
   - @RequestParam와 마찬가지로 생략이 가능하다.

   ```java
   @PostMapping("/events")
     @ResponseBody
     public Event postEvent(@ModelAttribute Event event) {
       return event;
     }
   }
   //-----Test------
    @Test
     public void postEvent() throws Exception {
       mockMvc.perform(post("/events")
                   .param("name", "keesun")
                   .param("limit", "20"))
               .andDo(print())
               .andExpect(status().isOk)
         		.andExpect(jsonPath("name").value("keesun"));
     }
   }
   ```

   - 여기서 limit 파라미터는 Integer 형이다. 만약 limit 값에 문자열이 들어온다면 ??

     - BindException 발생(400Error)
     - 바인딩 에러를 직접 다룰수 있다.

   - 바인딩 에러 직접 처리하기

     - BingingResult 타입의 아규먼트를 추가한다.

     ```java
     @PostMapping("/events")
       @ResponseBody
       public Event postEvent(@ModelAttribute Event event, BindingResult bindResult) {
         if(bindingReuslt.hasError()) {
           bindingResult.getAllErrors().forEach(c -> {
             System.out.println(c.toString());
           });
         }
         return event;
       }
     }
     ```

   - 또한 이러한 바인딩 에러 처리 이후 검증 작업을 추가로 하는것도 가능하다.

     - @Valid 또는 Validated 어노테이션을 사용하면 된다.

   ```java
    @PostMapping("/events")
     @ResponseBody
     public Event postEvent(@Valid @ModelAttribute Event event, BindingResult bindResult) {
       if(bindingReuslt.hasError()) {
         bindingResult.getAllErrors().forEach(c -> {
           System.out.println(c.toString());
         });
       }
       return event;
     }
   ```

   - @Valid와 @Validated의 차이는 검증 그룹화의 가능 유무이다.

5. ##### @SesstionAttributes

   - 모델 정보를 HTTP 세션에 저장해주는 어노테이션
   - 이 어노테이션에 설정한 이름에 해당하는 모델 정보를 자동으로 세션에 넣어준다.
   - @ModelAttribute는 세션에 있는 데이터도 바인딩 한다.
   - 여러 화면(또는 요청)에서 사용해야 하는 객체를 공유할 때 사용한다.

   - **SessionStatus**를 사용해서 세션 처리 완료를 알려 줄 수 있다.

   ```java
   @Controller
   @SessionAttributes("event")
   public class SampleController {
     
     @GetMapping("/events/form")
     public String eventsForm(Model model) {
       Event newEvent = new Event();
       newEvent.setLimit(50);
       model.addAttribute("event", newEvent);
       // httpSession.setAttribute("event", newEvent);
      	return "/events/form";
     }
   }
   //-----------Test------------
   @Test
     public void eventForm() throws Exception {
       mockMvc.perform(get("/events/form"))
         			.andDo(print())
         			.andExpect(view().name("/events/form"))
         			.andExpect(model().attributeExists("event"))
         			.andExpect(request().sessionAttribute("event", notNullValue()))
                   .andReturn().getRequest();
       Object event = request().getSession().getAttribute("event");
      	System.out.println(event);
     }
   }
   ```

6. 멀티 폼 서브밋

   **실행 흐름**

   - /events/form/name에서 name을 입력받는다.
   - /events/form/limit으로 redirect하여 limit을 입력받는다.
     - /events/form/name에서 Session에 등록된 Event를 @ModelAttribute를 통해 받는다.
   - Limit을 입력하여 Submit하면 Event List Page로 이동한다.

   ```java
   @Controller
   @SessionAttributes("event")
   public class SampleController {
     
     @GetMapping("/events/form/name")
     public String eventsForm(Model model) {
       Event newEvent = new Event();
       newEvent.setLimit(50);
       model.addAttribute("event", newEvent);
      	return "/events/form/name";
     }
     
     @PostMapping("/events/form/name")
     public String eventsFormNameSubmit(@Validated @ModelAttribute Event event, 
   													 BindingResult bindResult) {
       if(bindingReuslt.hasError()) {
         return "/events/form-name";
       }
       return "redirect:/events/form/limit";
     }
     
     @GetMapping("/events/form/limit")
     public String eventFromLimit(@ModelAttribute Event event, Model model) {
       model.addAttribute("event", event);
      	return "/events/form/limit";
     }
     
     @PostMapping("/events/form/limit")
     public String eventsFormLimitSubmit(@Validated @ModelAttribute Event event, 
   													 BindingResult bindResult,
                              SessionStatus sessionStatus) {
       if(bindingReuslt.hasError()) {
         return "/events/form-limit";
       }
       
       sessionStatus.setComplete();
       return "redirect:/events/list";
     }
     
     @GetMapping("events/list")
     public String getEvents(Model model) {
       Event event = new Event();
       event.setName("spring");
       event.setLimit(10);
       
       List<Event> eventList = new ArrayList<>();
       eventList.add(event);
       
       model.addAttribute(eventList);
       
       return "/events/list";
     }
   }
   ```

7. ##### @SessionAttribute

   - HttpSession을 사용할 때 비해 타입 컨버전을 자동으로 지원하기 때문에 조금 더 편리하다.
   - HTTP 세션에 데이터를 넣고 빼고싶은 경우에는 HttpSession을 사용하자.
     - HttpSession 사용시Object형으로 반환되기때문에 명시적으로 컨버전을 해줘야한다.
   - **@SessionAttributes**와는 완전히 다름으로 주의해야한다.
     - 이 어노테이션은 컨트롤러 내에서만 동작한다.
     - 즉, 해당 컨트롤러 안에서 다루는 특정 모델 객체를 세션에 넣고 공유할 때만 사용
   - **@SesstionAttribute**는 컨트롤러 밖(인터셉터/필터 등)에서 만들어준 세션 데이터에 접근할 때 사용한다.

   **Interceptor**

   ```java
   public class VisitTimeInterceptor implements HandlerInterceptor {
       @Override
       public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
           HttpSession session = request.getSession();
           if(session.getAttribute("visitTime") == null) {
               session.setAttribute("visitTime", LocalDateTime.now());
           }
           return true;
       }
   }
   ```

   **Interceptor 등록**

   ```java
   @Configuration
   public class WebConfig implements WebMvcConfigurer {
     
     @Override
     public void addInterceptor(InterceptorRegistry registry) {
       registry.addInterceptor(new VisitTImeInterceptor());
     }
   }
   ```

   **컨트롤러**

   ```java
    @GetMapping("events/list")
     public String getEvents(Model model, @SessionAttribute LocalDateTime visitTime) {
       // visitTime 출력
       System.out.println(visitTime);
       
       Event event = new Event();
       event.setName("spring");
       event.setLimit(10);
       
       List<Event> eventList = new ArrayList<>();
       eventList.add(event);
       
       model.addAttribute(eventList);
       
       return "/events/list";
     }
   ```

8. ##### RedirectAttributes

   Redirect 할 때 기본적으로 Model에 들어있는 primitive type 데이터를 URI쿼리 매개변수에 추가된다.

   - 스프링 부트에서는 이 기능이 기본적으로 비활성화 되어있다. (MVC만 사용하면 활성화)

   - ```
     # application.properties
     spring.mvc.ignore-default-model-on-redirect = true
     //추가하는 방법
     ```

   - Redirect할때 원하는 값만 전달하고 싶다면 RedirectAttributes에 명시적으로 추가할 수 있다.

   - 데이터를 받는 곳에서는 @RequestParam 이용해서 하나씩 받아도 되고 @ModelAttribute를 이용해서 받을 수도 있다.

     - @ModelAttribute 사용시 자동으로 객체에 값들이 채워진다.
     - 이것을 사용할때는 @SessionAttributes에서 사용하는 이름과 같은 이름 사용시 Session에서 우선적으로 찾기때문에 이름을 다르게 하거나, Session을 신경써야 한다.

   - ```java
         @PostMapping
         @ResponseBody
         public String eventsFormLimitSubmit(@ModelAttribute Event event,
                                             BindingResult bindingResult,
                                             SessionStatus sessionStatus,
                                             RedirectAttributes attributes) {
     
             if(bindingResult.hasErrors()) {
                 return "/event/form-limit";
             }
             sessionStatus.setComplete();
             //RedirectAttributes에 원하는 값만 추가하기
             attributes.addAttribute("name", event.getName());
             
             return "/event/form-list";
         }
     
     ```

9. ##### Flash Attributes

   위에서 사용한 RedirectAttributes를 통해 전달하는 데이터는 쿼리 파라미터에 붙을 수 있어야 하므로 전부 문자열로 변환이 가능해야 한다. 따라서 복합적인 **객체**를 전달하기는 어렵다.

   Flash Attributes를 사용하면 객체를 전달할 수 있는데, 이 때 객체는 HttpSession에 들어가고 Redirect된 곳에서 데이터가 사용되면 해당 데이터는 Session에서 제거된다.

10. ##### MultipartFile

    - 파일 업로드시 사용하는 메소드 아규먼트
    - MultipartResolver 빈이 설정 되어 있어야 사용할 수 있다.
      - 스프링부트가 자동으로 설정해준다.
    - POST multipart/form-data 요청에 들어있는 파일을 참조할 수 있다.

    ```java
    @Controller
    public class FileController {
      
      @GetMapping("/file")
      public String fileUploadForm() {
        return "files/index";
      }
      
      @PostMapping("/file")
      public String fileUpload(@RequestParam MultipartFile file) {
        // @RequestParam("file") MultipartFile file 과 같은 코드
        // 파일 업로드 폼에서 file이라는 이름으로 보내므로 이름을 file로 설정
        
        // save
        String message = file.getOriginalFileName() + "is uploaded";
        // message가 세션에 등록되고 redirect된 곳에서 사용 후 세션에서 제거된다.
        attributes.addFlashAttribute("message", message);
        return "redirect:/file";
      }
    }
    //---------Test----------
      @Test
      public void fileUploadTest() throws Exception {
        // Test를 위한 파일 생성
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "hello file".getBytes());
        
        this.mockMvc.perform(multipart("/file").file(file))
          			.andDo(print())
          			.andExpect(status().is3xxRedirection());
      }
    }
    ```

11. ##### @ResponseEntity

    - ResponseEntity는 사용자의 HttpRequest에 대한 응답 데이터를 포함하는 클래스이다.
    - 응답 상태 코드
    - 응답 헤더
    - 응답 본문

    **Tika 라이브러리 사용**

    - 파일 다운로드시 파일의 종류(MediaType)를 알아내기 위한 라이브러리
    - 의존성 등록이 필요하다.

    ```java
    @Controller
    public class FileController {
      
      @Autowired
      private ResourceLoader resourceLoader;
      
      @GetMapping("/file")
      public String fileUploadForm() {
        return "files/index";
      }
      
      @PostMapping("/file")
      public String fileUpload(@RequestParam MultipartFile file) {
        String message = file.getOriginalFileName() + "is uploaded";
        attributes.addFlashAttribute("message", message);
        return "redirect:/file";
      }
      
      @GetMapping("/file/{filename}")
      public ResponseEntity<Resource> fileDownload(@PathVariable String filename) throws IOException{
        Resource resource = resourceLoader.getResource("classpath:" + filename);
        File file = resource.getFile();
        
        Tika tika = new Tika();
        String mediaType = tika.detect(file);
        
        return ResponseEntity.ok()
          			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
          			.header(HttpHeaders.CONTENT_TYPE, mediaType)
          			.header(HttpHeaders.CONTENT_LENGTH, file.length() = "")
          			.body(resource);
      }
    }
    ```

12. ##### @RequestBody

    - 요청 본문(body)에 들어있는 데이터를 HttpMessageConverter를 통해 변환한 객체로 받아올 수 있다.
    - @Valid 또는 @Validated를 사용해서 값을 검증할 수 있다. (=@ModelAttribute와 동일한 방법)
    - BindingResult 아규먼트를 사용해 코드로 바인딩 또는 검증 에러를 확인할 수 있다.

    **MessageConverter**는 HandlerAdaptor가 아규먼트를 Resolving할때 사용한다.

    ```java
    @RestController
    @RequestMapping("/api/events")
    public class EventApi {
      
      @PostMapping
      public Event createEvent(@RequestBody @Valid Event event, BindingResult bindingResult) {
        //save event
        if(bindingResult.hasErrors()) {
          bindingResult.getAllErrors().forEach(error -> {
            System.out.println(error);
          });
        }
        
        return event;
      }
    }
    ```

    ```java
    @Test
      public void createEvent() throws Exception {
        Event event = new Event();
        event.setName("keesun");
        event.setLimit(20);
        
        String json = objectMapper.writeValueAsString(event);
        
        mockMvc.perform(post("/api/events")
                  .contentType(MediaType.APPLICATION_JSON_UTF8)
                  .content(json))
          			.andDo(print())
          			.andExpect(status().isOk)
          			.andExpect(jsonPath("name").value("keesun"))
          			.andExpect(jsonPath("limit").value(20));
      }
    }
    ```

    **HttpEntity**

    @RequestBody와 비슷하지만 추가적으로 요청 헤더 정보를 사용할 수 있다.

    ```java
     @PostMapping
      public Event createEvent(HttpEntity<Event> event) {
        //save event
        MediaType mediaType = event.getHeaders().getContentType();
        System.out.println(contentType);
        
        return request.getBody();
      }
    }
    ```

13. ##### @ResponseBody & @ResponseEntity

    **@ResponseBody**

    - 메소드에서 Return 하는 데이터를 Http 응답 본문(Body)에 담아준다.
      - HttpMessageConverter를 사용해 return하는 값을 응답 본문에 있는 메세지로 변환한다.
    - @RestController 사용시 자동으로 모든 핸들러 메소드에 적용된다.
      - @ResponseBody 어노테이션을 메타 어노테이션으로 사용하기 때문

    **@ResponseEntity**

    - 응답 헤더 상태 코드 본문을 직접 다루고 싶은 경우에 사용한다.

    ```java
    @Controller
    @RequestMapping("/api/events")
    public class EventApi {
      
      @PostMapping
      public ResponseEntity<Event> createEvent(@RequestBody @Vaild Event event, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
          return ResponseEntity.badRequest().build();
        }
        
        return ResponseEntity.ok(event);
        // 다음과 같이 세밀한 응답도 가능
        // return new ResponseEntity<Event>(event, HttpStatus.CREATED);
      }
    }
    ```

    ------

    
  </div>
</details>


<details>
  <summary>etc : @ModelAttribute, @InitBinder, @ControllerAdvice, @ExceptionHandler </summary>
  <div markdown="1">
    1. ##### @ModelAttribute

   모든 Handler(controller)에서 공통적으로 참조해야하는 Model 정보가 있는 경우 매번 모든 Handler에서 Model 정보를 추가하는 것은 귀찮은 작업이므로 다음과 같이 @ModelAttribute를 사용할 수 있다.

   ```java
   @Controller
   public class EventController{
       
       @ModelAttribute
       public void categories(Model model) {
           model.addAttribute("categories",list.of("comic,study,social"));
       }
   }
   ```

2. ##### @InitBinder

   특정 컨트롤러에서 바인딩 또는 검증 설정을 변경하고 싶을 때 사용한다.

   모든 요청 전에 @InitBinder 어노테이션이 부여된 Method가 실행된다.

   **바인딩 설정**

   - 특정 필드의 값을 받고싶지 않을때 설정한다.
   - 여기서는 id값을 받지 않게된다.
   - webDataBinder.setDisallowedFields();

   ```java
   @InitBinder
   public void initEventBinder(WebDataBinder webDataBinder)
       webDataBinder.setDisallowedFileds("id");
   ```

   **포메터 설정**

   - webDataBinder.addCustomFormatter();

   ```java
   public class Event {
     
     @DateTimeFormat(iso = DateTimeFormat.iso.DATE)
     private LocalDate startDate;
     // 이러한 변환이 이루어지는 이유는 해당 역할 할 수 있는 formatter 기본적으로 들어있기 때문이다.
     
     public LocalDate getStartDate() {
       return startDate;
     }
     
     public void setStartDate(LocalDate startDate) {
       this.startDate = startDate;
     }
   }
   ```

   **Validator 설정**

   - webDataBinder.addValidators();
   - custom한 validator를 만든다.

   ```java
   public class EventValidator implements Validator {
     
       //support에 어떤 domain class를 검증하는지 명시해준다.
     @Override
     public boolean support(Class<?> clazz) {
       return Event.class.isAssignableForm(clazz);
     }
     
     @Override
     public void validate(Object target, Errors errors) {
       Event event = (Event)target;
       if(event.getName().equalsIgnoreCase("aaa")) {
         errors.rejectValue("name", "wrongValue", "the value is not allowed");
       }
     }
   }
   ```

   ```java
   @Controller
   public class EventController {
     
     @InitBinder
     public void initEventBinder(WebDataBinder webDataBinder) {
       webDataBinder.addValidators(new EventValidator());
     }
   }
   ```

3. ##### @ExceptionHandler

   특정 예외가 발생한 요청을 처리하는 핸들러를 정의

   ```java
   @Controller
   public class EventController {
     
     @ExceptionHandler
     public String eventErrorHandler(EventException exception, Model model) {
       // 사용할 수 있는 Argument는 글 하단 참고 링크에서 확인
       model.addAttribute("message", "event error");
       return "error"; // error.html return
     }
     
     @ExceptionHandler
     public String runtimeErrorHandler(RuntimeException exception Model model) {
       model.addAttribute("message", "runtime error");
       return "error";
     }
     
     @GetMapping("/events/form/name")
     public String eventsFormName(Model model) {
       throw new EventException();
     }
   }
   ```

   - 이러한 경우 get 요청으로 ``/events/form/name``을 요청하게되면 EventException이 발생하면서 EventException을 정의한 핸들러가 리턴하는 view로 Resolving 된다.

   ```java
   @Controller
   public class EventController {
     
     // 여러 Exception을 같이 처리할 경우
     @ExceptionHandler({EventException.class, RuntimeException.class})
     public String eventErrorHandler(RuntimeException exception, Model model) {
       // Exception Argument는 모두 처리할 수 있는 상위 타입의 Exception을 사용
       
       model.addAttribute("message", "runtime error");
       return "error";
     }
     
     @GetMapping("/events/form/name")
     public String eventsFormName(Model model) {
       throw new EventException();
     }
   }
   ```

   - 이렇게 한번에 여러 예외를 처리할수도 있다.

   **REST API의 경우 응답 본문에 에러에 대한 정보를 담아주고  상태코드를 설정하려면 ResponseEntity를 주로 사용한다.**

   ```java
    @ExceptionHandler
     public ResponseEntity errorHandler() {
       return ResponseEntity.badRequest().body("에러가 발생한 이유");
     }
   ```

4. ##### @ControllerAdvice

   예외처리, 바인딩 설정, 모델 객체등 모든 컨트롤러 전반에 걸쳐 적용하고 싶을때 사용한다.

   - @ExceptionHandler
   - @InitBinder
   - @ModelAttribute

   ```java
   @ControllerAdvice
   public class BaseController {
     
     @ExceptionHandler
     public String eventErrorHandler(EventException exception, Model model) {
       model.addAttribute("message", "event error");
     }
     
     @InitBinder("event")
     public void initEventBinder(WebDataBinder webDataBinder) {
       webDataBinder.setDisallowedFileds("id");
     }
     
     @ModelAttribute
     public void categories(Model model) {
       model.addAttribute("categories", List.of("study", "seminar", "hobby", "social"));
     }
   }
   ```

   **적용할 범위를 지정할 수도 있다.**

   - 특정 Annotation을 가지고 있는 컨트롤러에만 적용하기
   - 특정 패키지 이하의 컨트롤러에만 적용하기
   - 특정 클래스 타입에만 적용하기

   ```java
   @ControllerAdvice(assignableTypes = {EventController.class, EventApi.class})
   ```

   
  </div>
</details>



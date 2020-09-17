package cn.edu.csust.coursecard.service.impl;

import cn.edu.csust.coursecard.bean.*;
import cn.edu.csust.coursecard.common.CodeEnum;
import cn.edu.csust.coursecard.common.ReturnData;
import cn.edu.csust.coursecard.dao.ScoreDAO;
import cn.edu.csust.coursecard.dao.StuInfoDAO;
import cn.edu.csust.coursecard.exception.BaseException;
import cn.edu.csust.coursecard.service.JwService;
import cn.edu.csust.coursecard.task.ScheduledTasks;
import cn.edu.csust.coursecard.utils.CalendarUtil;
import cn.edu.csust.coursecard.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author zsw
 * @date 2019/11/18 18:55
 */
@Slf4j
@Service("jwService")
public class JwServiceImpl implements JwService {

	@Autowired
	StuInfoDAO stuInfoDAO;

	@Autowired
	RedisUtil redisUtil;

	private String defaultHeadImageDirect;


	/**
	 * 从教务系统获取code的url
	 */
	private static final String GET_CODE_URL = "http://xk.csust.edu.cn/Logon.do?method=logon&flag=sess";

	private static SimpleDateFormat daySdf = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat weekDaySdf = new SimpleDateFormat("EEEE");
	private static SimpleDateFormat monthAndDat = new SimpleDateFormat("MM-dd");
	private static SimpleDateFormat yearSdf = new SimpleDateFormat("yyyy");

	/**
	 * 教务系统登录的url
	 */
	private static final String LOGIN_URL = "http://xk.csust.edu.cn/Logon.do?method=logon";

	/**
	 * 教务系统学生主页的url
	 */
	private static final String XS_MAIN_JSP_URL = "http://xk.csust.edu.cn/jsxsd/framework/xsMain_new.jsp?t1=1";

	/**
	 * JSESSIONID匹配的正则表达式
	 */
	private static final Pattern JW_JSESSIONID_Patten = Pattern.compile("^JSESSIONID=.*");
	private static final String NOW_XUEQI_PREFIX = "now:xueqi";

	/**
	 * 获得现在是第几学期
	 *
	 * @return
	 */
	private static String getNowSemester(String cookie) {
		String url = "http://xk.csust.edu.cn/jsxsd/xskb/xskb_list.do";
		OkHttpClient okHttpClient = new OkHttpClient();
		Request getXueqi = new Request.Builder()
				.url(url)
				.header("Cookie", cookie)
				.build();
		try {
			Response response = okHttpClient.newCall(getXueqi).execute();
			String htmlText = response.body().string();
			response.close();
			Document document = Jsoup.parse(htmlText);
			Element xnxq01id = document.getElementById("xnxq01id");
			Elements select = xnxq01id.select("[selected=selected]");
			String text = select.text();
			return text;
		} catch (Exception e) {
			throw new BaseException(CodeEnum.JW_SYSTEM_ERROR.getCode(), "教务系统错误");
		}


	}

	private boolean isFirstSemester() {
		try {
			String nowXueqi = (String) redisUtil.get(NOW_XUEQI_PREFIX);
			String[] strings = nowXueqi.split("-");
			if ("1".equals(strings[2])) {
				//如果是第一学期
				return true;
			} else {
				return false;
				//如果是第二学期
			}
		} catch (Exception e) {
			throw new BaseException(CodeEnum.SYSTEM_ERROR.getCode(), "系统错误");
		}
	}


	private List<String> getAllSemester(String stuId) {
		Integer rxYear = Integer.valueOf(stuId.substring(0, 4));

		String nowXueqi = (String) redisUtil.get(NOW_XUEQI_PREFIX);
		Integer nowYear = Integer.valueOf(nowXueqi.substring(0, 4));
		List<String> list = new LinkedList<>();
		if (isFirstSemester()) {
			for (int i = rxYear; i <= nowYear; i++) {
				list.add(i + "-" + (i + 1) + "-1");
				list.add(i + "-" + (i + 1) + "-2");
			}
		} else {
			int i = rxYear;
			for (; i <= nowYear; i++) {
				list.add(i + "-" + (i + 1) + "-1");
				list.add(i + "-" + (i + 1) + "-2");
			}
			list.add(i + "-" + (i + 1) + "-1");
		}
		return list;

	}


	@Override
	public ReturnData getCourse(HttpServletRequest request, String cookie, String xueqi, int zc) {
		String url = "http://xk.csust.edu.cn/jsxsd/xskb/xskb_list.do";
		OkHttpClient okHttpClient = new OkHttpClient();
		FormBody formBody = new FormBody.Builder()
				.add("xnxq01id", xueqi)
				.add("zc", zc + "").build();

		Request getCourse = new Request.Builder()
				.url(url)
				.header("Cookie", cookie)
				.post(formBody)
				.build();

		try {
			Response response = okHttpClient.newCall(getCourse).execute();
			String htmlText = response.body().string();
			response.close();
			Document document = Jsoup.parse(htmlText);
			Elements trs = document.select("table").select("tr");
			WeekCourse[][] weekCourses = new WeekCourse[5][7];
			if (trs.size() <= 2) {
				return ReturnData.fail(CodeEnum.REQUEST_FAILED.getCode(), "教务系统暂无该课表");
			}
			for (int i = 1; i < 6; i++) {
				Elements kbcontent = trs.get(i).getElementsByClass("kbcontent");

				for (int j = 0; j < 7; j++) {
					Elements elements = kbcontent.get(j).select("font");
					String courseName = kbcontent.get(j).ownText();
					if (j == 0) {
						if (!"".equals(courseName)) {
							WeekCourse weekCourse = new WeekCourse();
							weekCourse.setCourseName(courseName);
							weekCourse.setTeacher(elements.select("[title=老师]").text().split("（")[0]);
							weekCourse.setAddress(elements.select("[title=教室]").text());
							weekCourse.setTime(elements.select("[title=周次(节次)]").text());
							weekCourses[i - 1][6] = weekCourse;
						}
						continue;
					}

					if (!"".equals(courseName)) {
						WeekCourse weekCourse = new WeekCourse();
						weekCourse.setCourseName(courseName);
						weekCourse.setTeacher(elements.select("[title=老师]").text().split("（")[0]);
						weekCourse.setAddress(elements.select("[title=教室]").text());
						weekCourse.setTime(elements.select("[title=周次(节次)]").text());

						weekCourses[i - 1][j - 1] = weekCourse;

					}
				}
			}
			return ReturnData.success(weekCourses);

		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException(CodeEnum.SYSTEM_ERROR.getCode(), "系统错误");
		}

	}

	@Override
	public ReturnData login(HttpServletRequest request, String username, String password, String agent) {
		try {
			String[] jwCode = getJwCode();
			String encoded = encodePsd(username, password, jwCode[1]);
			OkHttpClient okHttpClient = new OkHttpClient.Builder().followRedirects(false).build();
			FormBody formBody = new FormBody.Builder()
					.add("userAccount", username)
					.add("userPassword", "")
					.add("encoded", encoded).build();

			Request jwLoginRequest = new Request.Builder()
					.header("Cookie", "JSESSIONID=" + jwCode[0])
					.header("Host", "xk.csust.edu.cn")
					.header("Origin", "http://xk.csust.edu.cn")
					.header("Referer", "http://xk.csust.edu.cn/")
					.url(LOGIN_URL)
					.post(formBody)
					.build();
			Response response = okHttpClient.newCall(jwLoginRequest).execute();
			String updateCookieUrl = response.header("Location");
			response.close();
			if (updateCookieUrl == null) {
				return ReturnData.fail(CodeEnum.REQUEST_FAILED.getCode(), "账号或密码错误");
			}
			OkHttpClient updateCookieClient = new OkHttpClient.Builder().followRedirects(false).build();
			Request updateCookieRequest = new Request.Builder()
					.header("Cookie", jwCode[0])
					.header("Referer", "http://xk.csust.edu.cn/")
					.url(updateCookieUrl)
					.build();
			Response updateCookieResponse = updateCookieClient.newCall(updateCookieRequest).execute();
			String header = updateCookieResponse.header("Set-Cookie");
			updateCookieResponse.close();
			try {
				String[] split = header.split(";");
				for (String s : split) {
					if (JW_JSESSIONID_Patten.matcher(s).matches()) {
						// 登录成功
						Map<String, Object> data = new LinkedHashMap<>();
						data.put("cookie", s);
						String token = s.split("=")[1];
						data.put("token", token);
						String nowSemester;
						if (redisUtil.exist(NOW_XUEQI_PREFIX)) {
							nowSemester = (String) redisUtil.get(NOW_XUEQI_PREFIX);
						} else {
							nowSemester = getNowSemester(s);
							redisUtil.set(NOW_XUEQI_PREFIX, nowSemester, (long) (60 * 60 * 24));
						}
						data.put("nowXueqi", nowSemester);
						StuInfo stuInfo = stuInfoDAO.selectStuInfoByStuId(username);
						if (stuInfo == null) {
							//如果数据库里没有保存该学生的信息，则添加学生信息到数据库
							stuInfo = getStuInfo(s);
							stuInfoDAO.insertStuInfo(stuInfo);
						}
						data.put("stuInfo", stuInfo);
						data.put("nowDate", daySdf.format(new Date()));
						Integer[] nowWeekDate = getNowWeekDate(s);
						data.put("nowWeek", nowWeekDate[0]);
						data.put("totalWeek", nowWeekDate[1]);

						List<LoginInfo> loginInfos = (List<LoginInfo>) redisUtil.getObject(ScheduledTasks.LOGIN_INFO_PREFIX);
						LoginInfo loginInfo = LoginInfo.builder().userId(stuInfo.getId()).loginTime(new Date()).agent(agent).build();
						loginInfos.add(loginInfo);
						redisUtil.setObject(ScheduledTasks.LOGIN_INFO_PREFIX, loginInfos);
						redisUtil.set(USER_TOKEN_PREFIX + token, String.valueOf(stuInfo.getId()), TOKEN_EXPIRE);
						redisUtil.set(USER_ID_PREFIX + stuInfo.getId(), token, TOKEN_EXPIRE);
						redisUtil.set(USER_STUID_PREFIX + stuInfo.getId(), stuInfo.getStuId(), TOKEN_EXPIRE);
						return ReturnData.success(data);
					}
				}
				return ReturnData.fail(CodeEnum.REQUEST_FAILED.getCode(), "登录失败，用户名或密码错误");
			} catch (Exception e) {
				// 登录失败
				e.printStackTrace();
				return ReturnData.fail(CodeEnum.REQUEST_FAILED.getCode(), "登录失败，用户名或密码错误");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ReturnData.fail(CodeEnum.JW_SYSTEM_ERROR.getCode(), "教务系统无响应");
		}
	}

	@Override
	public ReturnData getKsap(HttpServletRequest request, String cookie, String xueqi) {
		String url = "http://xk.csust.edu.cn/jsxsd/xsks/xsksap_list";
		OkHttpClient okHttpClient = new OkHttpClient();
		FormBody formBody = new FormBody.Builder()
				.add("xqlbmc", "")
				.add("xnxqid", xueqi)
				.add("xqlb", "")
				.build();
		Request getKsapRequest = new Request.Builder()
				.url(url)
				.header("cookie", cookie)
				.header("Referer", "http://xk.csust.edu.cn/jsxsd/xsks/xsksap_query")
				.post(formBody)
				.build();
		try {
			Response response = okHttpClient.newCall(getKsapRequest).execute();
			String htmlText = response.body().string();
			Document document = Jsoup.parse(htmlText);
			response.close();
			Element dataList = document.getElementById("dataList");
			Elements trs = dataList.select("tr");

			List<Exam> examList = new LinkedList<>();
			if (trs.size() <= 2) {
				return ReturnData.fail(CodeEnum.REQUEST_FAILED.getCode(), "教务系统暂无该学期的考试安排");
			}

			for (int i = 1; i < trs.size(); i++) {
				Element element = trs.get(i);
				Elements tds = element.select("td");
				Exam exam = new Exam();
				for (int j = 0; j < tds.size(); j++) {
					switch (j) {
						case 1:
							exam.setCampus(tds.get(j).text());
							break;
						case 4:
							exam.setCourseName(tds.get(j).text());
							break;
						case 5:
							exam.setTeacher(tds.get(j).text());
							break;
						case 6:
							String time = tds.get(j).text();
							if (time != null && !"".equals(time.trim())) {
								String[] strings = time.split(" ");
								String[] times = strings[1].split("~");
								exam.setStartTime(strings[0] + " " + times[0]);
								exam.setEndTime(strings[0] + " " + times[1]);
							}
							break;
						case 7:
							exam.setAddress(tds.get(j).text());
							break;
						case 8:
							exam.setSeatNumber(tds.get(j).text());
							break;
						case 9:
							exam.setTicketNumber(tds.get(j).text());
							break;
						default:
							break;
					}
				}
				examList.add(exam);

			}

			return ReturnData.success(examList);
		} catch (Exception e) {
			e.printStackTrace();
			return ReturnData.fail(CodeEnum.SYSTEM_ERROR.getCode(), "系统错误");
		}
	}

	@Autowired
	ScoreDAO scoreDAO;

	@Override
	public ReturnData queryScore(HttpServletRequest request, String cookie, String xueqi) {
		String url = "http://xk.csust.edu.cn/jsxsd/kscj/cjcx_list";
		OkHttpClient okHttpClient = new OkHttpClient();
		FormBody formBody = new FormBody.Builder()
				.add("kksj", xueqi)
				.add("kcxz", "")
				.add("kmmc", "")
				.add("xsfs", "all")
				.add("fxkc", "2")
				.build();
		Request queryScoreRequest = new Request.Builder()
				.url(url)
				.header("Cookie", cookie)
				.header("Referer", "http://xk.csust.edu.cn/jsxsd/kscj/cjcx_query")
				.post(formBody)
				.build();

		try {
			Response response = okHttpClient.newCall(queryScoreRequest).execute();
			String htmlText = response.body().string();
			response.close();
			Document document = Jsoup.parse(htmlText);
			Elements trs = document.getElementById("dataList").select("tr");
			if (trs.size() < 2 || htmlText.contains("评教未完成，不能查询成绩！")) {
				return ReturnData.fail(CodeEnum.REQUEST_FAILED.getCode(), "评教未完成，请先完成评教");
			}
			if ("未查询到数据".equals(trs.get(1).child(0).ownText())) {
				return ReturnData.fail(CodeEnum.REQUEST_FAILED.getCode(), "暂无该学期成绩");
			}

			List<Score> scoreList = new LinkedList<>();
			for (int i = 1; i < trs.size(); i++) {
				Elements tds = trs.get(i).select("td");
				Score score = new Score();
				for (int j = 1; j < tds.size(); j++) {
					switch (j) {
						case 1:
							score.setXueqi(tds.get(j).ownText());
							break;
						case 3:
							score.setCourseName(tds.get(j).ownText());
							break;
						case 5:
							score.setScore(tds.get(j).text());
							Elements a = tds.get(j).select("a");
							String href = a.attr("href");
							if (href != null && !"".equals(href)) {
								String[] split = href.split("'");
								score.setPscjUrl(split[1]);
							}
							break;
						case 7:
							score.setType(tds.get(j).ownText());
							break;
						case 8:
							score.setXuefen(tds.get(j).ownText());
							break;
						case 10:
							score.setPoint(tds.get(j).ownText());
							break;
						case 12:
							score.setMethod(tds.get(j).ownText());
							break;
						case 14:
							score.setProperty(tds.get(j).ownText());
							break;
						case 15:
							score.setNature(tds.get(j).ownText());
							break;
						default:
							break;
					}
				}
				String stuId = String.valueOf(request.getAttribute("stuId"));
				scoreList.add(score);
			}
			return ReturnData.success(scoreList);

		} catch (Exception e) {
			e.printStackTrace();
			return ReturnData.fail(CodeEnum.JW_SYSTEM_ERROR.getCode(), "教务系统无响应");
		}
	}

	@Override
	public ReturnData getStuInfo(HttpServletRequest request, String cookie) {
		StuInfo stuInfo = getStuInfo(cookie);
		StuInfo stuInfoFromDb = stuInfoDAO.selectStuInfoByStuId(stuInfo.getStuId());
		stuInfo.setId(stuInfoFromDb.getId());
		return ReturnData.success(stuInfo);
	}

	@Override
	public ReturnData queryPscj(HttpServletRequest request, String cookie, String pscjUrl) {
		OkHttpClient okHttpClient = new OkHttpClient();
		HashMap<String, Integer> map = new HashMap<>();
		Request jwPscjQueryRequest = new Request.Builder()
				.header("Cookie", cookie)
				.url("http://xk.csust.edu.cn" + pscjUrl)
				.build();

		try {
			Response response = okHttpClient.newCall(jwPscjQueryRequest).execute();
			String htmlText = response.body().string();
			response.close();
			Document document = Jsoup.parse(htmlText);
			Elements elements = document.select("#dataList");
			Elements trs = elements.select("tr");
			Element nameTr = trs.get(0);
			Elements ths = nameTr.select("th");
			int index = 0;
			for (Element th : ths) {
				String name = th.ownText();
				if ("平时成绩".equals(name)) {
					map.put("pscj", index);
				} else if ("平时成绩比例".equals(name)) {
					map.put("pscjBL", index);

				} else if ("期末成绩".equals(name)) {
					map.put("qmcj", index);

				} else if ("期末成绩比例".equals(name)) {
					map.put("qmcjBL", index);

				} else if ("总成绩".equals(name)) {
					map.put("totalScore", index);
				} else if ("期中成绩".equals(name)) {
					map.put("qzcj", index);
				} else if ("期中成绩比例".equals(name)) {
					map.put("qzcjBL", index);
				}
				index++;
			}

			Element pscjTr = trs.get(1);
			Elements tds = pscjTr.select("td");
			PscjInfo pscjInfo = new PscjInfo();

			Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, Integer> entry = iterator.next();
				String key = entry.getKey();
				Integer value = entry.getValue();

				if ("pscj".equals(key)) {
					pscjInfo.setPscj(tds.get(value).ownText());
				} else if ("pscjBL".equals(key)) {
					pscjInfo.setPscjBL(tds.get(value).ownText());

				} else if ("qmcj".equals(key)) {
					pscjInfo.setQmcj(tds.get(value).ownText());

				} else if ("qmcjBL".equals(key)) {
					pscjInfo.setQmcjBL(tds.get(value).ownText());

				} else if ("totalScore".equals(key)) {
					pscjInfo.setScore(tds.get(value).ownText());

				} else if ("qzcj".equals(key)) {
					pscjInfo.setQzcj(tds.get(value).ownText());

				} else if ("qzcjBL".equals(key)) {
					pscjInfo.setQzcjBL(tds.get(value).ownText());
				}
			}

//			pscjInfo.setKscj(tds.get(7).ownText());
//			pscjInfo.setKscjBL(tds.get(8).ownText());
//			pscjInfo.setPscj(tds.get(3).ownText());
//			pscjInfo.setPscjBL(tds.get(4).ownText());
//			pscjInfo.setScore(tds.get(9).ownText());
			return ReturnData.success(pscjInfo);
		} catch (Exception e) {
			throw new BaseException(CodeEnum.REQUEST_FAILED.getCode(), "请求错误");
		}

	}

	@Override
	public byte[] getXszp(HttpServletRequest request, String cookie) {
		OkHttpClient okHttpClient = new OkHttpClient();
		Request jwXszpRequest = new Request.Builder()
				.header("Referer", "http://xk.csust.edu.cn/jsxsd/framework/xsMain_new.jsp?t1=1")
				.header("Cookie", cookie)
				.url("http://xk.csust.edu.cn/jsxsd/grxx/xszpLoad").build();
		try {
			Response zpResponse = okHttpClient.newCall(jwXszpRequest).execute();
			byte[] bytes = zpResponse.body().bytes();
			zpResponse.close();
			return bytes;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException(CodeEnum.SYSTEM_ERROR.getCode(), "系统错误");
		}
	}

	@Override
	public ReturnData getAllXueqi(HttpServletRequest request) {
		Integer userId = Integer.valueOf(request.getAttribute("userId").toString());
		String stuId = String.valueOf(redisUtil.get(USER_STUID_PREFIX + userId));
		return ReturnData.success(getAllSemester(stuId));
	}

	@Override
	public ReturnData getWeekDate(HttpServletRequest request, String cookie) {
		Integer[] nowWeekDate = getNowWeekDate(cookie);
		Date date = new Date();
		String weekDay = weekDaySdf.format(date);
		Integer weekIdByStr = WeekDayEnum.getWeekIdByStr(weekDay);
		int sub = 1 - weekIdByStr;
		String dayOfWeekMon = CalendarUtil.getDateOfDesignDay(date, daySdf, sub);
		Date monDate;
		try {
			monDate = daySdf.parse(dayOfWeekMon);
		} catch (ParseException e) {
			throw new BaseException(CodeEnum.SYSTEM_ERROR.getCode(), "系统错误");
		}
		List<WeekDay> weekDays = new ArrayList<>();
		int index = 0;

		for (int i = nowWeekDate[0]; i >= 1; i--) {
			WeekDay weekDayTemp = new WeekDay();
			weekDayTemp.setWeekId(i);
			weekDayTemp.setWeekMonStr(CalendarUtil.getDateOfDesignDay(monDate, daySdf, index * -1 * 7));
			index++;
			weekDays.add(weekDayTemp);
		}
		index = 1;
		for (int i = nowWeekDate[0] + 1; i <= nowWeekDate[1]; i++) {
			WeekDay weekDayTemp = new WeekDay();
			weekDayTemp.setWeekId(i);
			weekDayTemp.setWeekMonStr(CalendarUtil.getDateOfDesignDay(monDate, daySdf, index * 7));
			index++;
			weekDays.add(weekDayTemp);
		}
		Collections.sort(weekDays);
		return ReturnData.success(weekDays);
	}

	public static void main(String[] args) {

	}


	@Override
	public ReturnData getWeekCourse(HttpServletRequest request, String cookie, String weekTime) {
		OkHttpClient okHttpClient = new OkHttpClient();
		String url = "http://xk.csust.edu.cn/jsxsd/framework/main_index_loadkb.jsp";

		FormBody formBody = new FormBody.Builder()
				.add("rq", weekTime)
				.build();

		Request kbRequest = new Request.Builder()
				.header("Cookie", cookie)
				.header("Referer", "http://xk.csust.edu.cn/jsxsd/framework/xsMain_new.jsp?t1=1")
				.header("Origin", "http://xk.csust.edu.cn")
				.url(url)
				.post(formBody)
				.build();
		try {
			Response response = okHttpClient.newCall(kbRequest).execute();
			String htmlText = response.body().string();
			response.close();
			Document document = Jsoup.parse(htmlText);
			Elements trs = document.select("tbody").select("tr");

			Course[][] courses = new Course[5][7];
			int num = 0;
			for (int i = 0; i < 5; i++) {
				Elements tds = trs.get(i).select("td");
				for (int j = 1; j < 8; j++) {
					Elements p = tds.get(j).select("p");
					String title = p.attr("title");
					if (!"".equals(title.trim())) {
						String[] propertis = title.split("<br/>");
						Course course = new Course();
						for (int k = 0; k < 5; k++) {
							String[] split = propertis[k].split("：");
							switch (k) {
								case 0:
									if (split.length == 2) {
										course.setXuefen(split[1]);
									} else {
										course.setXuefen(null);
									}
									break;
								case 1:
									if (split.length == 2) {
										course.setProperty(split[1]);
									} else {
										course.setProperty(null);
									}
									break;
								case 2:
									if (split.length == 2) {
										course.setCouseName(split[1]);
									} else {
										course.setCouseName(null);
									}
									break;
								case 3:
									if (split.length == 2) {
										course.setTime(split[1]);
									} else {
										course.setTime(null);
									}
									break;
								case 4:
									if (split.length == 2) {
										course.setAddress(split[1]);
									} else {
										course.setAddress(null);
									}
									break;
								default:
									break;
							}

						}
						courses[i][j - 1] = course;
					}
					num++;
				}
			}
			return ReturnData.success(courses);
		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}


	}


	/**
	 * 获得现在是第几周和总周数
	 * 0位置为 第几周
	 * 1位置为 总周数
	 *
	 * @param cookie
	 * @return
	 */
	private Integer[] getNowWeekDate(String cookie) {
		String url = "http://xk.csust.edu.cn/jsxsd/framework/xsMain_new.jsp?t1=1";
		OkHttpClient okHttpClient = new OkHttpClient();
		Request xsMainRequest = new Request.Builder()
				.header("Cookie", cookie)
				.header("Referer", "http://xk.csust.edu.cn/jsxsd/framework/xsMain.jsp")
				.url(url)
				.build();

		try {
			Response response = okHttpClient.newCall(xsMainRequest).execute();
			String htmlText = response.body().string();
			response.close();
			Document document = Jsoup.parse(htmlText);
			Element body = document.body();
			Element liShowWeekDiv = body.getElementById("li_showWeek");
			Elements nowWeekDiv = body.getElementsByClass("main_text main_color");

			int nowWeek;
			try {
				nowWeek = Integer.parseInt(nowWeekDiv.text().substring(1, nowWeekDiv.text().length() - 1));
			} catch (Exception e) {
				nowWeek = -1;
			}
			int totalWeek;
			try {
				String totalWeekStr = liShowWeekDiv.text().split("/")[1];
				totalWeek = Integer.parseInt(totalWeekStr.substring(0, totalWeekStr.length() - 1));
			} catch (Exception e) {
				totalWeek = 20;
			}

			Integer[] nowWeekDate = new Integer[2];
			nowWeekDate[0] = nowWeek;
			nowWeekDate[1] = totalWeek;
			return nowWeekDate;
		} catch (Exception e) {
			throw new BaseException(CodeEnum.JW_SYSTEM_ERROR.getCode(), "教务系统无响应");
		}

	}


	/**
	 * 从教务系统获取一个 JSESSIONID和code
	 *
	 * @return
	 */
	private String[] getJwCode() {
		try {
			OkHttpClient okHttpClient = new OkHttpClient();
			Request jwCodeRequest = new Request.Builder().url(GET_CODE_URL).build();
			Response response = okHttpClient.newCall(jwCodeRequest).execute();
			String header = response.header("Set-Cookie");
			String[] split = header.split(";");
			String[] result = new String[2];
			for (String s : split) {
				if (JW_JSESSIONID_Patten.matcher(s).matches()) {
					result[0] = s.split("=")[1];
				}
			}
			result[1] = response.body().string();
			response.body().close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException(CodeEnum.JW_SYSTEM_ERROR.getCode(), "教务系统无响应");
		}
	}

	private static String encodePsd(String username, String password, String dataStr) {
		String[] splitCode = dataStr.split("#");
		String scode = splitCode[0];
		String sxh = splitCode[1];
		String code = username + "%%%" + password;
		String encode = "";
		for (int i = 0; i < code.length(); i++) {
			if (i < 20) {
				encode = encode + code.substring(i, i + 1) + scode.substring(0, Integer.parseInt(sxh.substring(i, i + 1)));
				scode = scode.substring(Integer.parseInt(sxh.substring(i, i + 1)), scode.length());
			} else {
				encode = encode + code.substring(i, code.length());
				i = code.length();
			}
		}
		return encode;
	}

	private StuInfo getStuInfo(String cookie) {
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
				.header("Cookie", cookie)
				.header("Host", "xk.csust.edu.cn")
				.header("Referer", "http://xk.csust.edu.cn/jsxsd/framework/xsMain.jsp")
				.url(XS_MAIN_JSP_URL)
				.build();
		try {
			Response response = client.newCall(request).execute();
			String htmlText = response.body().string();
			response.close();
			Document doc = Jsoup.parse(htmlText);
			Element body = doc.body();
			Elements stuInfoDivs = body.getElementsByClass("middletopdwxxcont");
			if (stuInfoDivs == null) {
				throw new BaseException(CodeEnum.REQUEST_FAILED.getCode(), "Cookie错误或已失效");
			}
			StuInfo stuInfo = new StuInfo();
			int index = 0;
			for (Element infoDiv : stuInfoDivs) {
				switch (index) {
					case 1:
						stuInfo.setName(infoDiv.text());
						break;
					case 2:
						stuInfo.setStuId(infoDiv.text());
						break;
					case 3:
						stuInfo.setCollege(infoDiv.text());
						break;
					case 4:
						stuInfo.setMajor(infoDiv.text());
						break;
					case 5:
						stuInfo.setClassName(infoDiv.text());
						break;
				}
				index++;
			}

			stuInfo.setRegisterTime(new Date());
			return stuInfo;
		} catch (Exception e) {
			throw new BaseException(CodeEnum.JW_SYSTEM_ERROR.getCode(), "教务系统无响应");
		}
	}
}

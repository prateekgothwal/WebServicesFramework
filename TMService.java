package com.thinking.machines.servlets;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import com.thinking.machines.beans.*;
import com.thinking.machines.students.*;
import com.thinking.machines.annotations.*;
import java.lang.annotation.Annotation;
import com.thinking.machines.model.*;
import com.thinking.machines.service.*;
import com.thinking.machines.operations.*;
import java.lang.reflect.*;
import com.google.gson.*;
public class TMService extends HttpServlet
{
public void doGet(HttpServletRequest rq,HttpServletResponse rs)
{
try
{
String url=rq.getRequestURL().toString();
String res="";
String [] urls=url.split("/");
String key="/"+urls[5]+"/"+urls[6];
System.out.println(key);
ServletContext sc=getServletContext();
AssignmentMay22Model model=(AssignmentMay22Model)sc.getAttribute("map");
if(model.map.containsKey(key))
{
if(rq.getAttribute("req")!=null)
{
Object sb=rq.getAttribute("req");
Service service=model.map.get(key);
int i=0;
Parameter [] parameter=service.method.getParameters();
Object [] objects=new Object[parameter.length];
for(Parameter p:parameter)
{
if(p.getParameterizedType().toString().endsWith("Request"))
{
objects[i]=rq;
}
else if(p.getParameterizedType().toString().endsWith("Response"))
{
objects[i]=rs;
}
else
{
String classString=p.getParameterizedType().toString().substring(6);
objects[i]=rq.getAttribute("req");
}
i++;
}
if(service.instance==null)
{
Class c=service.className;
Object obj=c.newInstance();
service.instance=obj;
service.method.invoke(obj,objects);
}
else
{
service.method.invoke(service.instance,objects);
}
if(service.method.getAnnotation(Forward.class)!=null)
{
RequestDispatcher rd=rq.getRequestDispatcher(service.method.getAnnotation(Forward.class).value());
rd.forward(rq,rs);
}
}

else if(rq.getQueryString()!=null)
{

Service service=model.map.get(key);
Object obj;
if(service.instance==null)
{
Class c=service.className;
obj=c.newInstance();
service.instance=obj;
}
else
{
obj=service.className.newInstance();
}
String queryString=rq.getQueryString();
String [] queryArray=queryString.split("&");
HashMap<String,Object> keyValues=new HashMap<String,Object>();
String [] keyValue;
for(String j: queryArray)
{
keyValue=j.split("=");
keyValues.put(keyValue[0],keyValue[1]);
}
Parameter parameter[] =service.method.getParameters();
Object [] objects=new Object[parameter.length];
Annotation[][] annotations=service.method.getParameterAnnotations();
int i=0;
for(Annotation [] annotationRow: annotations)
{
for(Annotation annotation: annotationRow)
{
RequestData rd=(RequestData)annotation;
if(keyValues.containsKey(rd.value()))
{
if(parameter[i].getType().toString().equals("int") || parameter[i].getType().toString().endsWith("Integer"))
{
keyValues.put(rd.value(),Integer.parseInt(keyValues.get(rd.value()).toString()));
objects[i]=Integer.parseInt(keyValues.get(rd.value()).toString());
}
else if(parameter[i].getType().toString().equals("byte") || parameter[i].getType().toString().endsWith("Byte"))
{
keyValues.put(rd.value(),Byte.parseByte(keyValues.get(rd.value()).toString()));
objects[i]=Byte.parseByte(keyValues.get(rd.value()).toString());
}
else if(parameter[i].getType().toString().equals("short") || parameter[i].getType().toString().endsWith("Short"))
{
keyValues.put(rd.value(),Short.parseShort(keyValues.get(rd.value()).toString()));
objects[i]=Short.parseShort(keyValues.get(rd.value()).toString());
}
else if(parameter[i].getType().toString().equals("long") || parameter[i].getType().toString().endsWith("Long"))
{
keyValues.put(rd.value(),Long.parseLong(keyValues.get(rd.value()).toString()));
objects[i]=Long.parseLong(keyValues.get(rd.value()).toString());
}
else if(parameter[i].getType().toString().equals("float") || parameter[i].getType().toString().endsWith("Float"))
{
keyValues.put(rd.value(),Float.parseFloat(keyValues.get(rd.value()).toString()));
objects[i]=Float.parseFloat(keyValues.get(rd.value()).toString());
}
else if(parameter[i].getType().toString().equals("double") || parameter[i].getType().toString().endsWith("Double"))
{
keyValues.put(rd.value(),Double.parseDouble(keyValues.get(rd.value()).toString()));
objects[i]=Double.parseDouble(keyValues.get(rd.value()).toString());
}
else if(parameter[i].getType().toString().equals("boolean") || parameter[i].getType().toString().endsWith("Boolean"))
{
keyValues.put(rd.value(),Boolean.parseBoolean(keyValues.get(rd.value()).toString()));
objects[i]=Boolean.parseBoolean(keyValues.get(rd.value()).toString());
}
else
{
objects[i]=keyValues.get(rd.value()).toString();
}
}
}
i++;
}
res=(String)service.method.invoke(obj,objects);
PrintWriter pw=rs.getWriter();
pw.print(res);
}
else
{
Service service=model.map.get(key);
Object obj;
if(service.instance==null)
{
Class c=service.className;
obj=c.newInstance();
service.instance=obj;
res=(String)service.method.invoke(obj);
}
else
{
res=(String)service.method.invoke(service.instance);
}
PrintWriter pw=rs.getWriter();
pw.print(res);
}
}
else
{
res="The URL that you have typed is wrong";
}
}catch(Exception e)
{
e.printStackTrace();
}
}

public void doPost(HttpServletRequest rq,HttpServletResponse rs)
{
try
{
String url=rq.getRequestURL().toString();
String res="";
String [] urls=url.split("/");
String key="/"+urls[5]+"/"+urls[6];
ServletContext sc=getServletContext();
AssignmentMay22Model model=(AssignmentMay22Model)sc.getAttribute("map");

BufferedReader br=rq.getReader();
StringBuilder sb=new StringBuilder();
String line;
while(true)
{
line=br.readLine();
if(line==null) break;
sb.append(line);
}
String jsonString=sb.toString();
Gson gson=new Gson();
AJAXResponse ajaxResponse=new AJAXResponse();
if(model.map.containsKey(key))
{
Service service=model.map.get(key);
if(service.instance==null)
{
Class c=service.className;
Object obj=gson.fromJson(jsonString,Class.forName(service.path));
service.instance=obj;

Annotation[][] annotations=service.method.getParameterAnnotations();
String classString;
for(Annotation [] annotationRow: annotations)
{
for(Annotation annotation: annotationRow)
{
ClassType ct=(ClassType)annotation;
classString=ct.value().toString();
}
}
ResponseType responseType=service.method.getAnnotation(ResponseType.class);
if(responseType.value().equals("Nothing"))
{
res="";
ajaxResponse.setResponse(res);
}
else if(responseType.value().equals("HTML/text"))
{
res=(String)service.method.invoke(obj,obj);
ajaxResponse.setResponse(res);
}
else
{
ajaxResponse.setResponse(service.method.invoke(obj,obj));
}
}
else
{
ResponseType responseType=service.method.getAnnotation(ResponseType.class);
if(responseType.value().equals("Nothing"))
{
res="";
ajaxResponse.setResponse(res);
}
else if(responseType.value().equals("HTML/text"))
{
res=(String)service.method.invoke(service.instance,service.instance);
ajaxResponse.setResponse(res);
}
else
{
ajaxResponse.setResponse(service.method.invoke(service.instance,service.instance));
}
}
}
else
{
res="The URL that you have typed is wrong";
}
ajaxResponse.setSuccess(true);
ajaxResponse.setException("");
rs.setContentType("application/json");
PrintWriter pw=rs.getWriter();
String responseString=gson.toJson(ajaxResponse);
pw.print(responseString);
}catch(Exception e)
{
e.printStackTrace();
}
}
}
package com.thinking.machines.servlets;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import com.thinking.machines.model.*;
import com.thinking.machines.service.*;
import com.thinking.machines.annotations.*;
public class Initializer extends HttpServlet
{
public static ArrayList<String> files=new ArrayList<String>();
public HashMap<String,Service> map=new HashMap<String,Service>();
public static String path="";

static void RecursivePrint(File[] arr,int index,int level) 
{ 
if(index == arr.length) return;
if(arr[index].isFile())
{ 
if(arr[index].getName().endsWith(".class"))
{
if(!arr[index].getName().endsWith("Path.class") && !arr[index].getName().endsWith("ClassType.class") && !arr[index].getName().endsWith("Secure.class") && !arr[index].getName().endsWith("Forward.class") && !arr[index].getName().endsWith("RequestData.class") && !arr[index].getName().endsWith("ResponseType.class"))
{
String k=path.substring(54);
String l=k.replace("\\",".")+"."+arr[index].getName().replace(".class","");
files.add(new String(l)); 
}
}
}
else if(arr[index].isDirectory()) 
{
path="";
path+=arr[index];
RecursivePrint(arr[index].listFiles(), 0, level + 1); 
}
RecursivePrint(arr,++index, level);
}

public void init()
{
try
{
ServletContext sc=getServletContext();
AssignmentMay22Model assignmentMay22Model;
AssignmentMay22Model m=new AssignmentMay22Model();

String maindirpath = sc.getRealPath("")+File.separator+"WEB-INF"+File.separator+"classes";
File maindir = new File(maindirpath);
if(maindir.exists() && maindir.isDirectory()) 
{
File arr[] = maindir.listFiles();
RecursivePrint(arr,0,0);
Service service;
for(String s: files)
{
Class c=Class.forName(s);
Annotation [] annotations=c.getAnnotations();
for(Annotation annotation: annotations)
{
Path myAnno=(Path) annotation;
Method [] methods=c.getMethods();
for(Method mm:methods)
{
if(mm.getName().equals("wait")) break;
service=new Service();
service.className=Class.forName(s);
service.path=s;
service.method=c.getDeclaredMethod(mm.getName(),mm.getParameterTypes());
map.put(myAnno.value()+mm.getAnnotation(Path.class).value(),service);
}
}
}
}
else
{
System.out.println("Invalid Directory");
}

m.map=map;
sc.setAttribute("map",m);
}catch(ClassNotFoundException cnfe)
{
cnfe.printStackTrace();
}
catch(NoSuchMethodException nsme)
{
nsme.printStackTrace();
}
}
}
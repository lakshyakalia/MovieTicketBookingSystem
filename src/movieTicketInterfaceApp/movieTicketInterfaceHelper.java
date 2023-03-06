package movieTicketInterfaceApp;


/**
* movieTicketInterfaceApp/movieTicketInterfaceHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from movieTicketInterface.idl
* Monday, March 6, 2023 3:41:32 o'clock PM EST
*/

abstract public class movieTicketInterfaceHelper
{
  private static String  _id = "IDL:movieTicketInterfaceApp/movieTicketInterface:1.0";

  public static void insert (org.omg.CORBA.Any a, movieTicketInterfaceApp.movieTicketInterface that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static movieTicketInterfaceApp.movieTicketInterface extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (movieTicketInterfaceApp.movieTicketInterfaceHelper.id (), "movieTicketInterface");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static movieTicketInterfaceApp.movieTicketInterface read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_movieTicketInterfaceStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, movieTicketInterfaceApp.movieTicketInterface value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static movieTicketInterfaceApp.movieTicketInterface narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof movieTicketInterfaceApp.movieTicketInterface)
      return (movieTicketInterfaceApp.movieTicketInterface)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      movieTicketInterfaceApp._movieTicketInterfaceStub stub = new movieTicketInterfaceApp._movieTicketInterfaceStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static movieTicketInterfaceApp.movieTicketInterface unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof movieTicketInterfaceApp.movieTicketInterface)
      return (movieTicketInterfaceApp.movieTicketInterface)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      movieTicketInterfaceApp._movieTicketInterfaceStub stub = new movieTicketInterfaceApp._movieTicketInterfaceStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}

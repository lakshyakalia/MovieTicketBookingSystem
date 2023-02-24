# MovieTicketBookingSystem

> Movie Ticket Booking System using CORBA 

> Generate Stubs and Skeleton: (Go to src/) idlj –fall MovieTicket.idl

> #### Command to compile:
> javac Client/*.java Server/*.java Constants/*.java Interface/*.java Services/*.java movieTicketInterfaceApp/*.java


> #### Steps to run application: 
> 1) Open terminal and run: orbd -ORBInitialPort 1050 -ORBInitialHost localhost (for starting the ORB)
> 2) Go to src/ then run: java Client.Client -ORBInitialPort 1050 -ORBInitialHost localhost (for starting the client
> 3) Go to src/ then run: java Server.ServerInstance -ORBInitialPort 1050 -ORBInitialHost localhost (for starting the server)

> #### Version Required: JDK 8


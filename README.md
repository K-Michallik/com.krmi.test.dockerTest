# Modbus_URCap

## A tool modbus RTU URCap.
The URCap has been updated to run with SW6 on a Docker daemon (based on the work done here: https://github.com/EbbeFuglsang/Modbus_URCap). The following script functions are added by this URCap:
 
*	**init_tool_modbus(address)** where port is string and address is an int.
*	**tool_modbus_write(register_address, data)** both parameter is an int.
*	**tool_modbus_read(register_address)** parameter is an int.

The RS485 settings is controlled with the built in function:

    set_tool_communication(enabled,baud_rate,parity,stop_bits,rx_idle_chars,tx_idle_chars)


And tool supply voltage is controlled with:

    set_tool_voltage(voltage)

## An example script program (with OnRobot VGP20):
    set_tool_voltage(24)
    set_tool_communication(True, 1000000, 2, 1, 1.5, 3.5)

    init_tool_modbus(65)

    while True:
                   tool_modbus_write(0,60)
                   sleep(1)
                   tool_modbus_write(0,0)
                   sleep(1)
    end
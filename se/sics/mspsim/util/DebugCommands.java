/**
 * Copyright (c) 2008, Swedish Institute of Computer Science.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the Institute nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE INSTITUTE AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE INSTITUTE OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 * This file is part of MSPSim.
 *
 * $Id$
 *
 * -----------------------------------------------------------------
 *
 * CommandBundle
 *
 * Author  : Joakim Eriksson, Niclas Finne
 * Created : Mon Feb 11 2008
 * Updated : $Date: 2007/10/21 21:17:34 $
 *           $Revision: 1.3 $
 */
package se.sics.mspsim.util;
import se.sics.mspsim.core.CPUMonitor;
import se.sics.mspsim.core.MSP430;

public class DebugCommands implements CommandBundle {

  public void setupCommands(ComponentRegistry registry, CommandHandler ch) {
    final MSP430 cpu = (MSP430) registry.getComponent(MSP430.class);
    if (cpu != null) {
      ch.registerCommand("break", new Command() {
        public int executeCommand(final CommandContext context) {
          int baddr = context.getArgumentAsAddress(0);
          cpu.setBreakPoint(baddr,
              new CPUMonitor() {
                public void cpuAction(int type, int adr, int data) {
                  context.out.println("*** Break at " + adr);
                }
          });
          context.out.println("Breakpoint set at: " + baddr);
          return 0;
        }

        public String getArgumentHelp(CommandContext context) {
          return "<address>";
        }

        public String getCommandHelp(CommandContext context) {
          return "adds a breakpoint to a given address or symbol";
        }
      });

      ch.registerCommand("watch", new Command() {
        public int executeCommand(final CommandContext context) {
          int baddr = context.getArgumentAsAddress(0);
          cpu.setBreakPoint(baddr,
              new CPUMonitor() {
                public void cpuAction(int type, int adr, int data) {
                  context.out.println("*** Write: " + adr + " = " + data);
                }
          });
          context.out.println("Watch set at: " + baddr);
          return 0;
        }

        public String getArgumentHelp(CommandContext context) {
          return "watch";
        }

        public String getCommandHelp(CommandContext context) {
          return "adds a write watch to a given address or symbol";
        }
      });

    }
  }
}

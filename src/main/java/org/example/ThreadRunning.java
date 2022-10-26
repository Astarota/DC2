package org.example;

import java.io.IOException;

/**
 * Processor of HTTP request.
 */

public class ThreadRunning
        extends Thread
{
    private final ThreadSafeQueue<Struct> queue;

    public ThreadRunning(ThreadSafeQueue<Struct> queue) {
        this.queue = queue;
    }

    public void StartProcessor(Struct input) throws IOException {
        Processor proc = new Processor(input.socket, input.request);
        proc.process();
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Wait for new element.
                Struct elem = queue.pop();

                // Stop consuming if null is received.
                if (elem == null) {
                    return;
                }

                // Process element.
                StartProcessor(elem);
            }
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
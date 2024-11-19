import threading
import cv2
import numpy as np
import queue
import os

def average_color_block(image_array, x, y, square_size):
    block = image_array[y:y + square_size, x:x + square_size]
    avg_color = block.mean(axis=(0, 1)).astype(int)
    image_array[y:y + square_size, x:x + square_size] = avg_color
    return avg_color

class ImageProcessingThread(threading.Thread):
    def __init__(self, x_start, y_start, x_end, y_end, square_size, image_array, update_queue):
        super().__init__()
        self.x_start = x_start
        self.y_start = y_start
        self.x_end = x_end
        self.y_end = y_end
        self.square_size = square_size
        self.image_array = image_array
        self.update_queue = update_queue

    def run(self):
        for y in range(self.y_start, self.y_end, self.square_size):
            for x in range(self.x_start, self.x_end, self.square_size):
                average_color_block(self.image_array, x, y, self.square_size)
                
                self.update_queue.put(self.image_array.copy())  


def single_threaded_processing(image_array, square_size, display_update_func):
    h, w, _ = image_array.shape
    for y in range(0, h, square_size):
        for x in range(0, w, square_size):
            average_color_block(image_array, x, y, square_size)
            display_update_func(image_array)  


def multi_threaded_processing(image_array, square_size, display_update_func):
    h, w, _ = image_array.shape
    num_threads = 4  
    region_height = h // num_threads

    
    update_queue = queue.Queue()

    threads = []
    for i in range(num_threads):
        y_start = i * region_height
        y_end = (i + 1) * region_height if i != num_threads - 1 else h
        thread = ImageProcessingThread(0, y_start, w, y_end, square_size, image_array, update_queue)
        threads.append(thread)
        thread.start()  

    
    while any(thread.is_alive() for thread in threads) or not update_queue.empty():
        try:
            
            updated_image = update_queue.get(timeout=0.1)  
            display_update_func(updated_image)  
        except queue.Empty:
            pass

    # Wait for all threads to finish
    for thread in threads:
        thread.join()  # Main thread waits for each thread to finish

    
    display_update_func(image_array)




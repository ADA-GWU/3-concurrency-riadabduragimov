<h1 align = "center">Concurrency</h1>

# Table of Contents

1. [Overview](#overview)
2. [Background](#background)
   - 2.1 [Image Processing and Block-wise Averaging](#image-processing-and-block-wise-averaging)
   - 2.2 [Concurrency in Programming](#concurrency-in-programming)
   - 2.3 [Synchronization in Multi-threaded Programming](#synchronization-in-multi-threaded-programming)

3. [Java Implementation](#java-implementation)
   - 3.1 [Overview of Java Programs (Main.java and Main1.java)](#overview-of-java-programs-mainjava-and-main1java)
   - 3.2 [Single-threaded Image Processing in Main.java and Main1.java](#single-threaded-image-processing-in-mainjava-and-main1java)
   - 3.3 [Multi-threaded Processing in Main.java using ExecutorService](#multi-threaded-processing-in-mainjava-using-executorservice)
   - 3.4 [Multi-threaded Processing in Main1.java with CountDownLatch and ReentrantLock](#34-multi-threaded-processing-in-main1java-with-countdownlatch-and-reentrantlock)
   - 3.5 [Use of AtomicInteger for Synchronization in Main1.java](#35-use-of-atomicinteger-for-synchronization-in-main1java)
   - 3.6 [Key Differences and Challenges in Java Implementations](#36-key-differences-and-challenges-in-java-implementations)

4. Python Implementation
   - 4.1 [Libraries Used: OpenCV and threading](#41-libraries-used-opencv-and-threading)
   - 4.2 [Single-threaded Image Processing Approach](#42-single-threaded-image-processing-approach)
   - 4.3 [Multi-threaded Image Processing Approach using Python's threading module](#43-multi-threaded-image-processing-approach-using-pythons-threading-module)
   - 4.4 [Thread Management and Synchronization in Python](#44-thread-management-and-synchronization-in-python)
   - 4.5 [Challenges Encountered in Python Implementation](#45-challenges-encountered-in-python-implementation)

5. Performance Analysis
   - 5.1 [Performance Comparison of Single-threaded vs. Multi-threaded Approaches](#51-performance-comparison-of-single-threaded-vs-multi-threaded-approaches)
   - 5.2 [Threading Performance in Python (GIL Considerations)](#52-threading-performance-in-python-gil-considerations)
   - 5.3 [Performance Optimization Techniques](#53-performance-optimization-techniques)

6. User Interface
   - 6.1 [Interface Design for Image Loading and Processing Mode Selection](#61-interface-design-for-image-loading-and-processing-mode-selection)
   - 6.2 [Real-time Image Display and Updates](#62-real-time-image-display-and-updates)
   - 6.3 [Saving Processed Image and User Interaction](#63-saving-processed-image-and-user-interaction)
7. [How to Run the Project](#how-to-run-the-project)
8. [Conclusion](#conclusion)
   - 8.1 [Summary of Findings from the Java and Python Implementations](#summary-of-findings-from-the-java-and-python-implementations)
   - 8.2 [Lessons Learned from Multi-threading and Synchronization](#lessons-learned-from-multi-threading-and-synchronization)
   - 8.3 [Potential Future Improvements](#potential-future-improvements)

9. [References](#references)



# Overview
In this project, I focused on implementing and comparing single-threaded and multi-threaded approaches to image processing, specifically using block-wise averaging to progressively blur images. The project explored the application of concurrency and synchronization techniques in both Java and Python.

Initially, I developed two Java programs, Main.java and Main1.java, that process images in single-threaded and multi-threaded modes. I designed these programs to average the colors of blocks within an image and update a GUI in real-time. In Main.java, I implemented multi-threading using the ExecutorService with a fixed thread pool, whereas in Main1.java, I used manual thread management with CountDownLatch and ReentrantLock for synchronization. I also introduced an AtomicInteger to safely track the number of processed blocks in the multi-threaded approach. These Java implementations helped me gain insight into handling concurrency and synchronization in multi-threaded applications.

Building on this experience, I developed a Python version of the project, using OpenCV for image manipulation and Python’s threading module for concurrency. I began by implementing the single-threaded approach and later transitioned to a multi-threaded approach, where I divided the image into regions and assigned each region to a separate thread. I used Python’s threading module and a queue for safe communication between threads and the main image processing function.

Throughout the project, I focused on understanding and applying the principles of multi-threading and synchronization, as well as improving performance in image processing tasks. I also created a user-friendly interface that allows users to load an image, specify block sizes, and choose between single-threaded and multi-threaded processing modes.
 

# Background
## Image Processing and Block-wise Averaging
Image processing is a fundamental technique used to manipulate and analyze digital images to improve their quality, extract useful information, or prepare images for further analysis in applications like computer vision and machine learning. In this project, the focus is on processing images through block-wise averaging, a method that divides the image into smaller, non-overlapping blocks and computes the average pixel value for each block. This technique is particularly useful for tasks like noise reduction and image smoothing, where the goal is to reduce the high-frequency noise while retaining the important structural information of the image.

For the project, I implemented block-wise averaging as a preprocessing step. The image was divided into smaller square blocks, and each block's average pixel value was calculated and assigned to that block's central pixel. This operation helps in reducing the detail in the image while preserving larger structures, which is often useful for applications such as image compression or preparing an image for feature extraction.
 
For this project, I have used this logic of Block-wise Averaging:

## Concurrency in Programming
Concurrency in programming refers to the ability to execute multiple tasks simultaneously, allowing the system to perform better when dealing with time-consuming operations. In the case of image processing, particularly when handling large images or multiple images, concurrency allows tasks like dividing the image into blocks, processing each block, and performing computations on those blocks to be done in parallel, significantly improving the processing time.

In this project, I explored two approaches to concurrency: single-threaded processing and multi-threaded processing. The single-threaded approach, implemented in Java's Main.java, processes the entire image in a sequential manner. While this is straightforward, it can be inefficient for larger images, as each operation must wait for the previous one to complete.

To address this, I implemented multi-threaded processing using both Java's ExecutorService in Main.java and more advanced thread management in Main1.java using CountDownLatch and ReentrantLock. The use of multi-threading enables parallel processing of image blocks, allowing the system to perform multiple computations at the same time and thereby reducing the overall processing time.

## Synchronization in Multi-threaded Programming
Synchronization is essential when working with multi-threaded programs, especially when threads share common resources, such as images or data. In a multi-threaded image processing environment, where multiple threads might be processing different parts of the same image or performing computational tasks concurrently, synchronization ensures that these operations do not conflict with one another and lead to race conditions or data corruption.

In my implementation, synchronization was handled through different mechanisms in Java. In Main1.java, I used CountDownLatch to coordinate thread execution, ensuring that all threads finished processing their respective image blocks before proceeding to the next stage. Additionally, ReentrantLock was used to provide mutual exclusion, preventing multiple threads from accessing shared resources simultaneously. The use of AtomicInteger in the multi-threaded environment also ensured thread-safe increments, preventing issues when updating shared counters.

In Python, I used the threading module to implement multi-threaded processing. The threading.Lock was used to synchronize the threads, ensuring that only one thread at a time could modify shared resources or perform operations that could conflict. This approach allowed for concurrent image processing while maintaining data integrity.

# Java Implementation
## Overview of Java Programs (Main.java and Main1.java)
The two Java programs, Main.java and Main1.java, perform image processing tasks where the goal is to divide an image into smaller square blocks, compute the average color of each block, and then fill each block with the calculated average color. Both programs can process images either in a single-threaded or multi-threaded manner, offering different approaches to improve performance and visualization.
### Main.java
Main.java is designed for image processing with both single-threaded and multi-threaded modes. The program starts by loading an image using ImageIO.read(), followed by initializing a graphical user interface (GUI) that displays the image being processed. The program expects three command-line arguments: the image file name, the square block size, and the processing mode (S for single-threaded and M for multi-threaded).

In the single-threaded mode (S), the program processes each block sequentially, updating the GUI as each block is processed. The singleThreadedProcessing method iterates through the image and processes each square block by calculating its average color.

In multi-threaded mode (M), the program divides the image into several horizontal regions and assigns each region to a separate thread. The multi-threaded processing is handled by the multiThreadedProcessing method, which uses Java's ExecutorService to manage the threads. Each thread processes a portion of the image, and once all threads are complete, the processed image is saved as "result.jpg."

The program also includes a custom ImagePanel class for displaying the image in a GUI window and includes sleep delays to slow down the processing for visualization purposes.
### Main1.java
Main1.java follows a similar approach but introduces some key differences in its handling of multi-threading and synchronization. Like Main.java, the program processes an image by dividing it into square blocks, calculating the average color for each block, and filling the block with the computed color. However, Main1.java introduces more advanced synchronization techniques, including the use of an AtomicInteger to count the number of processed blocks and a ReentrantLock to ensure thread safety when updating the image.

In this program, multi-threaded processing is done using Runnable tasks. Each thread processes a different portion of the image, similar to Main.java, but now each block's processing is synchronized to avoid race conditions. The multiThreadedProcessingWithAtomic method uses a CountDownLatch to wait for all threads to finish before proceeding. It also uses a lock to ensure that updates to the GUI are done safely, as concurrent access to the image could cause inconsistencies.

The synchronization ensures that threads do not interfere with each other while updating the shared image object. After processing is completed, the program also outputs the total number of processed blocks and saves the result as "result.jpg."

## Single-threaded Image Processing in Main.java and Main1.java

In this project, I have implemented single-threaded image processing in both **Main.java** and **Main1.java**. The image is processed block by block, with each block being analyzed individually. The approach involves iterating over the image in small square regions of pixels, calculating the average color for each block, and then applying this average color to the corresponding block in the image. This allows the program to modify the image progressively.

### Common Process:
- I have divided the image into blocks of a specified size, defined by `squareSize`. This block division ensures that the processing is done in manageable sections.
- The `averageColorBlock()` method is called for each block, calculating the average color of the pixels within that block. The average color is then applied to the entire block, updating the image.
- After each block is processed, I have implemented a small delay of 50 milliseconds using `sleep(50)`, which ensures that the image update is visible step by step, allowing users to observe the changes in real-time.

### Execution Flow:
1. **Initialization**: In this project, I first determine the dimensions of the image and set the block size before starting the processing.
2. **Block Processing**: The image is iterated over in steps of `squareSize`, and I process each block sequentially. The average color for each block is calculated and then applied to the corresponding region of the image.
3. **Image Update**: Once a block is processed, I update the image to reflect the new color values. This progressive update helps visualize the image transformation.
4. **Delay**: I have added a small delay after each block is processed to make the changes visible to the user in real-time.

For this project, I have followed a single-threaded approach, which processes each block one after the other. While this approach works fine for smaller images, it is not as efficient for larger images or for scenarios where faster processing is needed. This single-threaded method is easier to understand and implement but does not take advantage of the full potential of multi-core processors.



## Multi-threaded Processing in `Main.java` using `ExecutorService`

In this project, I have implemented multi-threaded image processing in **Main.java** using Java's `ExecutorService` to achieve parallel processing of image blocks. This approach allows the program to process multiple blocks concurrently, taking advantage of multiple CPU cores to significantly speed up the image processing.

### ExecutorService Overview:
- **ExecutorService** is a higher-level API in Java that manages and controls a pool of threads, providing an efficient way to handle concurrent tasks. In this project, I have used `ExecutorService` to submit image blocks for parallel processing.
- By dividing the image into smaller blocks, I am able to assign each block to a separate thread, reducing the overall time required for processing.

### Implementation Details:
1. **Initialization**: I first initialize the `ExecutorService` with a fixed thread pool using `Executors.newFixedThreadPool(int nThreads)`. The number of threads in the pool is set based on the number of available processor cores, allowing the program to scale according to the system's capacity.
   
2. **Block Division**: The image is divided into square blocks of a given size (defined by `squareSize`). These blocks are processed concurrently, where each block is handled by a separate thread.

3. **Submitting Tasks**: I submit each block for processing using the `submit()` method of `ExecutorService`. Each task calculates the average color of the block and applies the color to the corresponding region of the image.

4. **Processing with Multiple Threads**: By using `ExecutorService`, multiple blocks are processed simultaneously. This allows for a more efficient use of the system's CPU resources, especially on multi-core processors, as each block is handled by a different thread. The program does not wait for one block to finish before starting the next; instead, it runs multiple blocks in parallel.

5. **Shutdown**: After submitting all the tasks, I call `shutdown()` on the `ExecutorService` to stop the service after all tasks are completed. This ensures that the program does not submit any new tasks and properly releases the resources allocated for the threads.

### Benefits of Multi-threading:
- **Improved Performance**: The main advantage of using `ExecutorService` in this project is the significant speed improvement. By processing blocks concurrently, the program can handle larger images in less time compared to the single-threaded approach.
- **Efficient CPU Utilization**: Multi-threading ensures that all available CPU cores are utilized, leading to more efficient processing. This is particularly beneficial when working with large images or when real-time image processing is required.

For this project, I have implemented multi-threaded image processing to leverage the benefits of parallelism, significantly enhancing performance when processing large images. Although multi-threading introduces some complexity in handling concurrent tasks, the improvements in execution time make it a worthwhile approach for this kind of image processing task.




import cv2
import numpy as np
import matplotlib.pyplot as plt
import argparse
import sys
from datetime import datetime
from concurrency import multi_threaded_processing, single_threaded_processing

def display_update(image_array, square_size):
    plt.clf()  
    plt.imshow(cv2.cvtColor(image_array, cv2.COLOR_BGR2RGB))  
    plt.axis('off')

    # Add grid lines
    h, w, _ = image_array.shape
    for x in range(0, w, square_size):
        plt.axvline(x, color='white', linestyle='--', linewidth=0.5)
    for y in range(0, h, square_size):
        plt.axhline(y, color='white', linestyle='--', linewidth=0.5)

    plt.draw()
    plt.pause(0.05)  

# Event handler to exit the program when the window is closed
def on_close(event):
    print("Window closed. Exiting program.")
    sys.exit()

def load_image(file_name):
    image = cv2.imread(file_name)
    if image is None:
        raise ValueError("Error: Unable to load image.")
    return image

# Command-line arguments
def parse_arguments():
    parser = argparse.ArgumentParser(description="Image processing with progressive averaging.")
    parser.add_argument("file_name", type=str, help="Path to the image file (jpg format)")
    parser.add_argument("square_size", type=int, help="Side length of each averaging square")
    parser.add_argument("mode", choices=["S", "M"], help="Processing mode: S (single-threaded) or M (multi-threaded)")
    return parser.parse_args()

def save_result(image_array, output_file="result.jpg"):
    cv2.imwrite(output_file, image_array)

def main():
    start_time = datetime.now()

    args = parse_arguments()
    image = load_image(args.file_name)
    image_array = image.copy()  

    plt.ion()
    display_update(image_array, args.square_size)  # Initial display with grid
    plt.show()

    # Add close event handler
    plt.gcf().canvas.mpl_connect('close_event', on_close)

    if args.mode == "M":
        print("Running multi-threaded processing...")
        multi_threaded_processing(image_array, args.square_size, lambda img: display_update(img, args.square_size))
    elif args.mode == "S":
        print("Running single-threaded processing...")
        single_threaded_processing(image_array, args.square_size, lambda img: display_update(img, args.square_size))

    print("Processing complete. Result saved as result.jpg.")
    save_result(image_array)
    
    end_time = datetime.now()
    duration = end_time - start_time

    minutes = duration.seconds // 60
    seconds = duration.seconds % 60
    print(f"Program execution time: {minutes} minutes and {seconds} seconds.")
    
    plt.ioff()
    plt.imshow(cv2.cvtColor(image_array, cv2.COLOR_BGR2RGB))  
    plt.show()

    plt.close()

if __name__ == "__main__":
    main()

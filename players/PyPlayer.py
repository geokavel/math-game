import random
import string
import socket

socket = socket.socket()
socket.connect(("",7777))
socket.sendall("PyPlayer\n")
input = socket.recv(16)
while input.find("quit")==-1:
    nums = input.split(" ")
    target = nums.pop(-1)
    samp = random.sample(nums,3)
    s=string.join(samp,"+")
    for i in samp:
        nums.remove(i)
    s += "*"+string.join(nums,"")
    socket.sendall(s+"\n")
    input = socket.recv(16)


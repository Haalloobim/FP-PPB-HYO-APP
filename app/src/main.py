from flask import Flask, request, jsonify
import cv2
import numpy as np
from ultralytics import YOLO
import io
import torch

app = Flask(__name__)
print(torch.cuda.is_available())  # Should print True
print(torch.cuda.current_device())
print(torch.cuda.get_device_name(torch.cuda.current_device()))
# Load YOLO model
model = YOLO('model.pt').to('cuda')  # Ganti dengan path model Anda

# check gpu cuda
print(model.device)  # Pastikan model berjalan di GPU


@app.route('/predict', methods=['POST'])
def predict():
    if 'image' not in request.files:
        return jsonify({'error': 'No image part'}), 400

    file = request.files['image']
    if file.filename == '':
        return jsonify({'error': 'No selected file'}), 400

    # Read image
    in_memory_file = io.BytesIO()
    file.save(in_memory_file)
    data = np.frombuffer(in_memory_file.getvalue(), dtype=np.uint8)
    image = cv2.imdecode(data, cv2.IMREAD_COLOR)
    if image is None:
        return jsonify({'error': 'Cannot decode image'}), 400

    # Predict
    results = model.predict(source=image, conf=0.5, device='cuda:0')

    predictions = []
    for box in results[0].boxes:
        print(box)
        confidence = float(box.conf[0])
        class_id = int(box.cls[0])
        label = model.names[class_id]
        predictions.append({
            'label': label,
            'confidence': round(confidence, 4)
        })

    return jsonify({'predictions': predictions})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)

// Empty means the API is served by this same Spring Boot application.
// This works locally and after deployment without a hard-coded host name.
const API_BASE = "";
const API = `${API_BASE}/api/School`;

let students = [];

const $ = (id) => document.getElementById(id);
const table = $("studentTable");

function showToast(message) {
  const toast = $("toast");
  toast.textContent = message;
  toast.classList.add("show");
  setTimeout(() => toast.classList.remove("show"), 2500);
}

function setMessage(message, error = false) {
  const el = $("formMessage");
  el.textContent = message;
  el.style.color = error ? "#dc2626" : "#64748b";
}

function setApiStatus(online) {
  const status = $("apiStatus");
  status.innerHTML = `<i style="background:${online ? '#16a34a' : '#dc2626'}"></i> ${online ? 'API connected' : 'API unavailable'}`;
}

function renderStudents(list = students) {
  $("studentCount").textContent = students.length;
  if (!list.length) {
    table.innerHTML = `<tr><td colspan="5" class="empty">No students found.</td></tr>`;
    return;
  }
  table.innerHTML = list.map(s => `
    <tr>
      <td><div class="student-name">${escapeHtml(s.name ?? "-" )}</div><div class="student-sub">Student ID: ${s.id ?? "-"}</div></td>
      <td>${escapeHtml(s.Roll_No ?? "-")}</td>
      <td>${escapeHtml(s.Course ?? "-")}</td>
      <td><span class="marks">${escapeHtml(s.marks ?? "-")}</span></td>
      <td><div class="actions">
        <button class="action edit" onclick="editStudent(${s.id})">Edit</button>
        <button class="action delete" onclick="deleteStudent(${s.id})">Delete</button>
      </div></td>
    </tr>`).join("");
}

function escapeHtml(value) {
  return String(value).replace(/[&<>'"]/g, c => ({"&":"&amp;","<":"&lt;",">":"&gt;","'":"&#39;",'"':"&quot;"}[c]));
}

async function loadStudents() {
  table.innerHTML = `<tr><td colspan="5" class="empty">Loading students...</td></tr>`;
  try {
    const response = await fetch(`${API}/readall`);
    if (!response.ok) throw new Error(`HTTP ${response.status}`);
    students = await response.json();
    setApiStatus(true);
    renderStudents();
  } catch (error) {
    setApiStatus(false);
    table.innerHTML = `<tr><td colspan="5" class="empty">Could not load students.<br><small>Make sure Spring Boot is running on port 8080.</small></td></tr>`;
    console.error(error);
  }
}

async function saveStudent(event) {
  event.preventDefault();
  const id = $("studentId").value;
  const payload = {
    name: $("name").value.trim(),
    Roll_No: Number($("rollNo").value),
    Course: $("course").value.trim(),
    marks: $("marks").value.trim()
  };

  const isEdit = Boolean(id);
  const url = isEdit ? `${API}/update/${id}` : API;

  try {
    const response = await fetch(url, {
      method: isEdit ? "PUT" : "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    });
    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(errorText || `HTTP ${response.status}`);
    }
    showToast(isEdit ? "Student updated successfully" : "Student added successfully");
    resetForm();
    await loadStudents();
  } catch (error) {
    setMessage("Request failed. Check your backend and input values.", true);
    console.error(error);
  }
}

window.editStudent = function(id) {
  const student = students.find(s => Number(s.id) === Number(id));
  if (!student) return;
  $("studentId").value = student.id;
  $("name").value = student.name ?? "";
  $("rollNo").value = student.Roll_No ?? "";
  $("course").value = student.Course ?? "";
  $("marks").value = student.marks ?? "";
  $("formMode").textContent = "EDIT STUDENT";
  $("formTitle").textContent = "Update student";
  $("submitBtn").innerHTML = "✓ Save Changes";
  $("cancelEdit").classList.remove("hidden");
  window.scrollTo({ top: 0, behavior: "smooth" });
};

window.deleteStudent = async function(id) {
  const student = students.find(s => Number(s.id) === Number(id));
  if (!confirm(`Delete ${student?.name || "this student"}?`)) return;
  try {
    const response = await fetch(`${API}/${id}`, { method: "DELETE" });
    if (!response.ok) throw new Error(`HTTP ${response.status}`);
    showToast("Student deleted successfully");
    await loadStudents();
  } catch (error) {
    showToast("Could not delete student");
    console.error(error);
  }
};

function resetForm() {
  $("studentForm").reset();
  $("studentId").value = "";
  $("formMode").textContent = "ADD STUDENT";
  $("formTitle").textContent = "Create a student";
  $("submitBtn").innerHTML = "<span>＋</span> Add Student";
  $("cancelEdit").classList.add("hidden");
  setMessage("");
}

$("studentForm").addEventListener("submit", saveStudent);
$("cancelEdit").addEventListener("click", resetForm);
$("refreshBtn").addEventListener("click", loadStudents);
$("search").addEventListener("input", (event) => {
  const q = event.target.value.toLowerCase().trim();
  const filtered = students.filter(s =>
    String(s.name ?? "").toLowerCase().includes(q) ||
    String(s.Course ?? "").toLowerCase().includes(q) ||
    String(s.Roll_No ?? "").toLowerCase().includes(q)
  );
  renderStudents(filtered);
});

loadStudents();

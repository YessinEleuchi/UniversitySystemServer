window.addEventListener('DOMContentLoaded', () => {
    if (window.CYCLE_MODAL_OPEN) {
        const modal = new bootstrap.Modal(document.getElementById('cycleModal'));
        modal.show();
    }
});

window.openCreateModal = function () {
    const form = document.getElementById('cycleForm');
    form.action = '/admin/cycles';
    document.getElementById('cycleId').value = '';
    document.getElementById('cycleCode').value = '';
    document.getElementById('cycleLabel').value = '';
};
